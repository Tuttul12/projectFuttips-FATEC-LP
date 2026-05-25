package com.futtips.project.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cargo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CargoEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;

    @NotBlank(message = "A permissão do cargo é obrigatória.")
    @Size(max = 20, message = "A permissão deve ter no máximo 20 caracteres.")
    @Column(length= 20, nullable=false)
    private String permissao;
}
