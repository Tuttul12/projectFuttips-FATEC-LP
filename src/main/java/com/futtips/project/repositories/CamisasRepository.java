package com.futtips.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.futtips.project.entities.CamisasEntity;

public interface CamisasRepository extends JpaRepository <CamisasRepository, Integer> {

    List<CamisasEntity> bucasPorTipoIdTipo(Integer idTipo);

    
    List<CamisasEntity> bucasPorFuncionarioIdPessoa(Integer idFuncionario);

    
    List<CamisasEntity> bucasPorTamanho(String tamanho);
}
