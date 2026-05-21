package com.futtips.project.entities;

import java.io.Serializable;

import com.futtips.project.entities.pk.EnderecoPK;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="enderecos")
public class EnderecosEntity implements Serializable {
    @EmbeddedId
    private EnderecoPK id = new EnderecoPK();

    @Column(length = 100, nullable = false)
    private String rua;

    @Column(nullable = false)
    private int numero;

    @Column(length = 100, nullable = false)
    private String bairro;

    @Column(length = 100, nullable = false)
    private String cidade;

    @Column(length = 100, nullable = false)
    private String estado;

    @Column(length = 12, nullable = false)
    private String cep;


}
