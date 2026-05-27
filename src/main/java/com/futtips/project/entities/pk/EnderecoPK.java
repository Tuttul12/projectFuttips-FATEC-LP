package com.futtips.project.entities.pk;

import java.io.Serializable;

import com.futtips.project.entities.PessoasEntity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EnderecoPK implements Serializable {
    
    @NotNull(message = "A pessoa vinculada ao endereço é obrigatória.")
    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private PessoasEntity pessoasEntity;

    @NotNull(message = "O código do endereço é obrigatório.")
    private Integer idEndereco;
}
