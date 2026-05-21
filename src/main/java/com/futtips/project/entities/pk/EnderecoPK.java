package com.futtips.project.entities.pk;

import java.io.Serializable;

import com.futtips.project.entities.PessoasEntity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class EnderecoPK implements Serializable {
    
    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private PessoasEntity pessoasEntity;

    private Integer idEndereco;
}
