package com.futtips.project.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futtips.project.entities.PedidosEntity;
import com.futtips.project.repositories.PedidosRepository;

@Service
public class PedidosService {

    @Autowired
    private PedidosRepository pedidosRepository;

    public List<PedidosEntity> buscarTodos() {
        return pedidosRepository.findAll();
    }

    public Optional<PedidosEntity> buscarPedido(Integer id) {
        return pedidosRepository.findById(id);
    }

    public List<PedidosEntity> buscarPorCliente(Integer clienteId) {
        return pedidosRepository.buscaPorClienteId(clienteId);
    }

    public PedidosEntity criar(PedidosEntity pedido) {
        return pedidosRepository.save(pedido);
    }

    public void excluir(Integer id) {
        pedidosRepository.deleteById(id);
    }
}
