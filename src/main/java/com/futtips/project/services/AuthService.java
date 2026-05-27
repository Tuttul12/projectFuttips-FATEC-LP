package com.futtips.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futtips.project.entities.ClientesEntity;
import com.futtips.project.entities.FuncionariosEntity;
import com.futtips.project.entities.PessoasEntity;
import com.futtips.project.entities.dto.LoginRequestDTO;
import com.futtips.project.entities.dto.LoginResponseDTO;
import com.futtips.project.repositories.PessoasRepository;

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

        return new LoginResponseDTO(
            pessoa.getId(),
            pessoa.getNome(),
            pessoa.getEmail(),
            perfil
        );
    }
}
