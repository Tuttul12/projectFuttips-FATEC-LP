package com.futtips.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.futtips.project.entities.ItensPedidosEntity;

public interface ItensPedidosRepository extends JpaRepository<ItensPedidosEntity, Integer> {

    List<ItensPedidosEntity> buscaPorPedidoCodigo(Integer pedidoCodigo);

    
    List<ItensPedidosEntity> buscaPorCamisaIdCamisa(Integer idCamisa);
}
