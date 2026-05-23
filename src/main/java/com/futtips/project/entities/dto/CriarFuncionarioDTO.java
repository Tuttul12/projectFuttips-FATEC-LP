package com.futtips.project.entities.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriarFuncionarioDTO {

    private String nome;
    private String cpf;
    private String email;
    private String senha;
    private BigDecimal salario;
    private Integer codigoCargo;
}