package com.futtips.project.entities;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.futtips.project.entities.dto.RelatorioClienteDTO;

import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.SqlResultSetMapping;
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
@PrimaryKeyJoinColumn(name = "id_clientes")
@SqlResultSetMapping(
    name = "RelatorioClienteMapping",
    classes = @ConstructorResult(
        targetClass = RelatorioClienteDTO.class,
        columns = {
            @ColumnResult(name = "id_pessoa", type = Integer.class),
            @ColumnResult(name = "nome_cliente", type = String.class),
            @ColumnResult(name = "cpf", type = String.class),
            @ColumnResult(name = "total_pedidos", type = Integer.class),
            @ColumnResult(name = "valor_total_gasto", type = java.math.BigDecimal.class)
            }
        )
    )
public class ClientesEntity extends PessoasEntity {

    @Column(nullable = false)
    private Date nascimento;
    
    @Column(length= 20, nullable = false)
    private String telefone;

    @Column (nullable = false, name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @JsonIgnore
    @OneToMany
    private List<PedidosEntity> pedidos;

}
