package com.futtips.project.entities.dto;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditarClienteDTO {

    private String nome;
    private String cpf;
    private String email;
    private String senha;

    private Date nascimento;
    private String telefone;

    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
}