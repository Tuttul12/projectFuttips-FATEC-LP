package com.futtips.project.entities.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioParaClienteDTO {
    @NotNull(message = "A pessoa é obrigatória.")
    private Integer idPessoa;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve estar no passado.")
    private LocalDate nascimento;

    @NotBlank(message = "O telefone é obrigatório.")
    @Pattern(regexp = "^[0-9+()\\-\\s]{10,20}$", message = "Telefone inválido.")
    private String telefone;
}
