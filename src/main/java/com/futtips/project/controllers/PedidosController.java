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

import com.futtips.project.entities.PedidosEntity;
import com.futtips.project.entities.dto.CriarPedidoDTO;
import com.futtips.project.services.PedidosService;

@RestController
@RequestMapping("/pedidos")
public class PedidosController {
    @Autowired
    private PedidosService pedidosService;

    @GetMapping
    public List<PedidosEntity> buscarTodos() {
        return pedidosService.buscarTodos();
    }

    @GetMapping("/{id}")
    public Optional<PedidosEntity> buscarPedido(@PathVariable Integer id) {
        return pedidosService.buscarPedido(id);
    }

    // GET /pedidos/cliente/3
    @GetMapping("/cliente/{clienteId}")
    public List<PedidosEntity> buscarPorCliente(@PathVariable Integer clienteId) {
        return pedidosService.buscarPorCliente(clienteId);
    }

    @PostMapping
    public PedidosEntity criar(@RequestBody CriarPedidoDTO dto) {
        return pedidosService.criar(dto);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Integer id) {
        pedidosService.excluir(id);
    }

}
