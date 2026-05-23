package com.futtips.project.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futtips.project.entities.ItensPedidosEntity;
import com.futtips.project.services.ItensPedidosService;

@RestController
@RequestMapping("/itens-pedidos")
public class ItensPedidosController {

    @Autowired
    private ItensPedidosService itensPedidosService;

    @GetMapping
    public List<ItensPedidosEntity> buscarTodos() {
        return itensPedidosService.buscarTodos();
    }

    @GetMapping("/{id}")
    public Optional<ItensPedidosEntity> buscarPorId(@PathVariable Integer id) {
        return itensPedidosService.buscarPorId(id);
    }


    @GetMapping("/pedido/{pedidoCodigo}")
    public List<ItensPedidosEntity> buscarPorPedido(@PathVariable Integer pedidoCodigo) {
        return itensPedidosService.buscarPorPedido(pedidoCodigo);
    }

    // GET /itens-pedidos/camisa/3
    @GetMapping("/camisa/{idCamisa}")
    public List<ItensPedidosEntity> buscarPorCamisa(@PathVariable Integer idCamisa) {
        return itensPedidosService.buscarPorCamisa(idCamisa);
    }

    @PostMapping
    public ItensPedidosEntity criar(@RequestBody ItensPedidosEntity item) {
        return itensPedidosService.criar(item);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Integer id) {
        itensPedidosService.excluir(id);
    }
}
