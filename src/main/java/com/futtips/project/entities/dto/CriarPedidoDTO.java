package com.futtips.project.entities.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriarPedidoDTO {

    private Integer idCliente;
    private BigDecimal valor;
    private List<ItemPedidoDTO> itens;

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemPedidoDTO {
        private Integer idCamisa;
        private Integer qtd;
    }
}
