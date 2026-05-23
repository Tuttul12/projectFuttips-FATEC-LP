package com.futtips.project.entities.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriarClienteDTO {

    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private LocalDate nascimento;
    private String telefone;
}