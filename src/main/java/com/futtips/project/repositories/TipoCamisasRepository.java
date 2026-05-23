package com.futtips.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.futtips.project.entities.TipoCamisasEntity;

public interface TipoCamisasRepository extends JpaRepository<TipoCamisasEntity, Integer> {

    List<TipoCamisasEntity> findByFabricante(String fabricante);

    List<TipoCamisasEntity> findByModelo(String modelo);
}
