package com.futtips.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.futtips.project.entities.CamisasEntity;

public interface CamisasRepository extends JpaRepository <CamisasEntity, Integer> {

    List<CamisasEntity> findByTipoCamisasEntityIdTipo(Integer idTipo);

    // navega: funcionariosEntity → id (herdado de PessoasEntity)
    List<CamisasEntity> findByFuncionariosEntityId(Integer idFuncionario);

    
    List<CamisasEntity> findByTamanho(String tamanho);
}
