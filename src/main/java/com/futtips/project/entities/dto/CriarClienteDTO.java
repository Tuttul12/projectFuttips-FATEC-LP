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

    // Dados de Pessoa
    private String nome;
    private String cpf;
    private String email;
    private String senha;

    // Dados de Cliente
    private LocalDate nascimento;
    private String telefone;

    // Dados de Endereço
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
}