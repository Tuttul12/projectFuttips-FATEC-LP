package com.futtips.project.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="itens_pedidos")
public class ItensPedidosEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "A quantidade do item é obrigatória.")
    @Min(value = 1, message = "A quantidade do item deve ser maior que zero.")
    @Column(nullable = false)
    private Integer qtd;

    @NotNull(message = "O pedido do item é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private PedidosEntity pedido;

    @NotNull(message = "A camisa do item é obrigatória.")
    @ManyToOne
    @JoinColumn(name = "id_camisa", nullable = false)
    private CamisasEntity camisa;
}
