package com.futtips.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.futtips.project.entities.TipoCamisasEntity;

public interface TipoCamisasRepository extends JpaRepository<TipoCamisasRepository, Integer> {

    List<TipoCamisasEntity> buscaPorFabricante(String fabricante);

    List<TipoCamisasEntity> buscaPorModelo(String modelo);
}
