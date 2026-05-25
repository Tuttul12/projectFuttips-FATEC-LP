package com.futtips.project.entities;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class ClientesEntity extends PessoasEntity {

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve estar no passado.")
    @Column(nullable = false)
    private Date nascimento;
    
    @NotBlank(message = "O telefone é obrigatório.")
    @Pattern(regexp = "^[0-9+()\\-\\s]{10,20}$", message = "Telefone inválido.")
    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
    @Column(length= 20, nullable = false)
    private String telefone;

    @Column(nullable = false, name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @JsonIgnore
    @OneToMany
    private List<PedidosEntity> pedidos;
}
