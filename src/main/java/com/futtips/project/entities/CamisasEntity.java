package com.futtips.project.entities;

import java.io.Serializable;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "camisas")
public class CamisasEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCamisa;

    @Column(length = 100, nullable = false)
    private String descricao;

    @Column(length = 5, nullable = false)
    private String tamanho;

    @Column(nullable = true)
    private Integer quantidade;

    // FK → funcionarios (quem registrou)
    @ManyToOne
    @JoinColumn(name = "id_funcionario", nullable = false)
    private FuncionariosEntity funcionariosEntity;

    // FK → tipo_camisas
    @ManyToOne
    @JoinColumn(name = "tipo_camisa", nullable = false)
    private TipoCamisasEntity tipoCamisasEntity;

    // Relacionamento com itens de pedido
    @JsonIgnore
    @OneToMany(mappedBy = "camisa")
    private List<ItensPedidosEntity> itensPedidosEntity;
}
