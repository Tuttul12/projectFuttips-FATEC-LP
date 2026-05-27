package com.futtips.project.entities.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RelatorioClienteDTO {

    private Integer idPessoa;
    private String nomeCliente;
    private String cpf;
    private Integer totalPedidos;
    private BigDecimal valorTotalGasto;
}