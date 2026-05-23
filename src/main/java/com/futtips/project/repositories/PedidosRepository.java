package com.futtips.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.futtips.project.entities.PedidosEntity;

public interface PedidosRepository extends JpaRepository<PedidosEntity, Integer> {
    List<PedidosEntity> findByClientesEntityId(Integer clienteId);

}
