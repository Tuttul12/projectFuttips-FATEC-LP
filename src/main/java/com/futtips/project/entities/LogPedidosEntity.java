package com.futtips.project.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "log_pedidos")
public class LogPedidosEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_pedido", nullable = false)
    private Integer idPedido;

    @Column(length = 30, nullable = false)
    private String protocolo;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(name = "id_cliente", nullable = false)
    private Integer idCliente;

    @Column(name = "nome_cliente", nullable = false)
    private String nomeCliente;

    @Column(name = "data_pedido", nullable = false)
    private LocalDate dataPedido;

    @Column(name = "registrado_em", nullable = false)
    private LocalDateTime registradoEm;

    @Column(length = 10, nullable = false)
    private String operacao;
}