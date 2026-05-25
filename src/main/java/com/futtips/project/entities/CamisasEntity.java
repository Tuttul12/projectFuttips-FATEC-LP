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
import jakarta.validation.constraints.Min;
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
@Table(name = "camisas")
public class CamisasEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCamisa;

    @NotBlank(message = "A descrição da camisa é obrigatória.")
    @Size(max = 100, message = "A descrição deve ter no máximo 100 caracteres.")
    @Column(length = 100, nullable = false)
    private String descricao;

    @NotBlank(message = "O tamanho da camisa é obrigatório.")
    @Size(max = 5, message = "O tamanho deve ter no máximo 5 caracteres.")
    @Column(length = 5, nullable = false)
    private String tamanho;

    @NotNull(message = "A quantidade em estoque é obrigatória.")
    @Min(value = 0, message = "A quantidade em estoque não pode ser negativa.")
    @Column(nullable = false)
    private Integer quantidade;

    @NotNull(message = "O funcionário responsável pelo cadastro da camisa é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "id_funcionario", nullable = false)
    private FuncionariosEntity funcionariosEntity;

    @NotNull(message = "O tipo da camisa é obrigatório.")
    @ManyToOne
    @JoinColumn(name = "tipo_camisa", nullable = false)
    private TipoCamisasEntity tipoCamisasEntity;

    @JsonIgnore
    @OneToMany(mappedBy = "camisa")
    private List<ItensPedidosEntity> itensPedidosEntity;
}
