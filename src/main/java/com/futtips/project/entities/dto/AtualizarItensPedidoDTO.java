package com.futtips.project.entities.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AtualizarItensPedidoDTO {

    @NotNull(message = "O pedido é obrigatório.")
    private Integer idPedido;

    @Valid
    @NotEmpty(message = "A atualização precisa ter pelo menos um item.")
    private List<ItemPedidoDTO> itens;

    @Getter @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemPedidoDTO {
        @NotNull(message = "A camisa do item é obrigatória.")
        private Integer idCamisa;

        @NotNull(message = "A quantidade do item é obrigatória.")
        @Min(value = 1, message = "A quantidade do item deve ser maior que zero.")
        private Integer qtd;
    }
}
