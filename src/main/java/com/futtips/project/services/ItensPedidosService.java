package com.futtips.project.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futtips.project.entities.ItensPedidosEntity;
import com.futtips.project.repositories.ItensPedidosRepository;

@Service
public class ItensPedidosService {

    @Autowired
    private ItensPedidosRepository itensPedidosRepository;

    public List<ItensPedidosEntity> buscarTodos() {
        return itensPedidosRepository.findAll();
    }

    public Optional<ItensPedidosEntity> buscarPorId(Integer id) {
        return itensPedidosRepository.findById(id);
    }

    public List<ItensPedidosEntity> buscarPorPedido(Integer pedidoCodigo) {
        return itensPedidosRepository.buscaPorPedidoCodigo(pedidoCodigo);
    }

    public List<ItensPedidosEntity> buscarPorCamisa(Integer idCamisa) {
        return itensPedidosRepository.buscaPorCamisaIdCamisa(idCamisa);
    }

    public ItensPedidosEntity criar(ItensPedidosEntity item) {
        return itensPedidosRepository.save(item);
    }

    public void excluir(Integer id) {
        itensPedidosRepository.deleteById(id);
    }
}
