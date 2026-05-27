package com.futtips.project.entities.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CriarPedidoDTO {

    @NotNull(message = "O cliente do pedido é obrigatório.")
    private Integer idCliente;

    @NotNull(message = "O valor do pedido é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor do pedido deve ser maior que zero.")
    private BigDecimal valor;

    @Valid
    @NotEmpty(message = "O pedido precisa ter pelo menos um item.")
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
