package com.futtips.project.entities.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClienteParaFuncionarioDTO {
    @NotNull(message = "A pessoa é obrigatória.")
    private Integer idPessoa;

    @NotNull(message = "O salário é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O salário deve ser maior que zero.")
    private BigDecimal salario;

    @NotNull(message = "O cargo é obrigatório.")
    private Integer codigoCargo;
}
