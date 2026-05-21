package com.futtips.project.entities;

import java.math.BigDecimal;



import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
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
    
    private BigDecimal salario;

    @ManyToOne
    @JoinColumn(name= "codigo_cargo")
    private CargoEntity cargo;

}
