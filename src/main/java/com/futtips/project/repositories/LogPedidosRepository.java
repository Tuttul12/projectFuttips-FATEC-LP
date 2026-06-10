package com.futtips.project.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.futtips.project.entities.LogPedidosEntity;

public interface LogPedidosRepository extends JpaRepository<LogPedidosEntity, Integer> {

    // Buscar logs de um pedido específico
    List<LogPedidosEntity> findByIdPedido(Integer idPedido);

    // Buscar logs de um cliente específico
    List<LogPedidosEntity> findByIdCliente(Integer idCliente);

    // Buscar por operação: INSERT, UPDATE ou DELETE
    List<LogPedidosEntity> findByOperacao(String operacao);
}