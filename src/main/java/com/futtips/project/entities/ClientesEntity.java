package com.futtips.project.entities;

import java.time.Instant;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="clientes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "id_pessoa")
public class ClientesEntity extends PessoasEntity {

    @Column(nullable = false)
    private Date nascimento;
    
    @Column(length= 20, nullable = false)
    private String telefone;

    @Column (nullable = false, name = "data_cadastro")
    private Instant dataCadastro;
}
