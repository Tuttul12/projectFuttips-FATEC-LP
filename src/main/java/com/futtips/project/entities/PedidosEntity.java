package com.futtips.project.entities;

import java.io.Serializable;
import java.math.BigDecimal;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "pedidos")
public class PedidosEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;

    @Column(length=30, nullable=false)
    private String protocolo;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false, name="data_pedido")
    private Date dataPedido;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private ClientesEntity clientesEntity;

    @JsonIgnore
    @OneToMany(mappedBy = "pedido")
    private List<ItensPedidosEntity> itensPedidosEntity;
    
}
