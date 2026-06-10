package com.futtips.project.services;

import java.time.Instant;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;

import com.futtips.project.config.SecurityConfig;
import com.futtips.project.entities.ClientesEntity;
import com.futtips.project.entities.FuncionariosEntity;
import com.futtips.project.entities.PessoasEntity;
import com.futtips.project.entities.dto.LoginRequestDTO;
import com.futtips.project.entities.dto.LoginResponseDTO;
import com.futtips.project.repositories.PessoasRepository;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

@Service
public class AuthService {

    @Autowired
    private PessoasRepository pessoasRepository;

    public LoginResponseDTO login(LoginRequestDTO dto) {

        PessoasEntity pessoa = pessoasRepository.findByEmail(dto.getEmail())
            .orElseThrow(() -> new RuntimeException("E-mail não encontrado"));

        if (!pessoa.getAtivo()) {
            throw new RuntimeException("Usuário inativo");
        }

        if (!pessoa.getSenha().equals(dto.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }

        String perfil;

        if (pessoa instanceof FuncionariosEntity) {
            perfil = "ADMIN";
        } else if (pessoa instanceof ClientesEntity) {
            perfil = "CLIENTE";
        } else {
            perfil = "PESSOA";
        }

        String token = gerarToken(pessoa, perfil);

        return new LoginResponseDTO(
            pessoa.getId(),
            pessoa.getNome(),
            pessoa.getEmail(),
            perfil,
            token
        );
    }

    private String gerarToken(PessoasEntity pessoa, String perfil) {
        var key = new SecretKeySpec(SecurityConfig.getSecret().getBytes(), "HmacSHA256");
        var encoder = new NimbusJwtEncoder(new ImmutableSecret<>(key));

        Instant now = Instant.now();

        var claims = JwtClaimsSet.builder()
            .issuer("futtips-api")
            .issuedAt(now)
            .expiresAt(now.plusSeconds(60 * 60 * 2))
            .subject(pessoa.getEmail())
            .claim("idPessoa", pessoa.getId())
            .claim("nome", pessoa.getNome())
            .claim("perfil", perfil)
            .claim("scope", perfil)
            .build();

        var header = JwsHeader.with(MacAlgorithm.HS256).build();

        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}