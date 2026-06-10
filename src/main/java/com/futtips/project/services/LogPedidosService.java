package com.futtips.project.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.futtips.project.entities.LogPedidosEntity;
import com.futtips.project.repositories.LogPedidosRepository;

@Service
public class LogPedidosService {

    @Autowired
    private LogPedidosRepository logPedidosRepository;

    public List<LogPedidosEntity> buscarTodos() {
        return logPedidosRepository.findAll();
    }

    public List<LogPedidosEntity> buscarPorPedido(Integer idPedido) {
        return logPedidosRepository.findByIdPedido(idPedido);
    }

    public List<LogPedidosEntity> buscarPorCliente(Integer idCliente) {
        return logPedidosRepository.findByIdCliente(idCliente);
    }

    public List<LogPedidosEntity> buscarPorOperacao(String operacao) {
        return logPedidosRepository.findByOperacao(operacao);
    }
}