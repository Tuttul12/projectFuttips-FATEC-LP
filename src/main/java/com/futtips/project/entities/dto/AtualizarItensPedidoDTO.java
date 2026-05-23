package com.futtips.project.entities.dto;

import java.util.List;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarItensPedidoDTO {

    private Integer idPedido;
    private List<ItemPedidoDTO> itens;

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemPedidoDTO {
        private Integer idCamisa;
        private Integer qtd;
    }
}