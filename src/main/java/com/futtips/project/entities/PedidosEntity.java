package com.futtips.project.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @NotBlank(message = "O protocolo do pedido é obrigatório.")
    @Size(max = 30, message = "O protocolo deve ter no máximo 30 caracteres.")
    @Column(length=30, nullable=false)
    private String protocolo;

    @NotNull(message = "O valor do pedido é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O valor do pedido deve ser maior que zero.")
    @Column(nullable = false)
    private BigDecimal valor;

    @NotNull(message = "A data do pedido é obrigatória.")
    @Column(nullable = false, name="data_pedido")
    private Date dataPedido;

    @NotNull(message = "O cliente do pedido é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private ClientesEntity clientesEntity;

    @JsonIgnore
    @OneToMany(mappedBy = "pedido")
    private List<ItensPedidosEntity> itensPedidosEntity;
}
