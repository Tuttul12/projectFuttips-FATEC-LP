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

    @Column(nullable = false)
    private Integer qtd;

    // FK para pedidos
    @ManyToOne
    @JoinColumn(name = "id_pedido", nullable = false)
    private PedidosEntity pedido;

    // FK para camisas
    @ManyToOne
    @JoinColumn(name = "id_camisa", nullable = false)
    private CamisasEntity camisa;
}
