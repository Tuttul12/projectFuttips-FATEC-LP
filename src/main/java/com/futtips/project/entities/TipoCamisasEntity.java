package com.futtips.project.entities;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "tipo_camisas")
public class TipoCamisasEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipo;

    @NotBlank(message = "O modelo da camisa é obrigatório.")
    @Size(max = 50, message = "O modelo deve ter no máximo 50 caracteres.")
    @Column(length = 50, nullable = false)
    private String modelo;

    @NotBlank(message = "O fabricante é obrigatório.")
    @Size(max = 20, message = "O fabricante deve ter no máximo 20 caracteres.")
    @Column(length = 20, nullable = false)
    private String fabricante;

    @JsonIgnore
    @OneToMany(mappedBy = "tipoCamisasEntity")
    private List<CamisasEntity> camisasEntity;
}
