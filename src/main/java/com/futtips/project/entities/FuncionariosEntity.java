package com.futtips.project.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="funcionarios")
@PrimaryKeyJoinColumn(name = "id_pessoa")
public class FuncionariosEntity extends PessoasEntity {
    
    @NotNull(message = "O salário é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O salário deve ser maior que zero.")
    @Column(nullable = false)
    private BigDecimal salario;

    @NotNull(message = "O cargo do funcionário é obrigatório.")
    @ManyToOne
    @JoinColumn(name= "codigo_cargo", nullable = false)
    private CargoEntity cargo;
}
