package com.futtips.project.entities.dto;

import java.time.LocalDate;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioParaClienteDTO {
    private Integer idPessoa;
    private LocalDate nascimento;
    private String telefone;
}
