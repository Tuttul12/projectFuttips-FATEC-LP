package com.futtips.project.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {

    private Integer idPessoa;
    private String nome;
    private String email;
    private String perfil;
    private String token;
}