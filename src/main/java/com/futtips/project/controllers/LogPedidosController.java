package com.futtips.project.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.futtips.project.entities.LogPedidosEntity;
import com.futtips.project.services.LogPedidosService;

@RestController
@RequestMapping("/log-pedidos")
public class LogPedidosController {

    @Autowired
    private LogPedidosService logPedidosService;

    @GetMapping
    public List<LogPedidosEntity> buscarTodos() {
        return logPedidosService.buscarTodos();
    }

    // GET /log-pedidos/pedido/1
    @GetMapping("/pedido/{idPedido}")
    public List<LogPedidosEntity> buscarPorPedido(@PathVariable Integer idPedido) {
        return logPedidosService.buscarPorPedido(idPedido);
    }

    // GET /log-pedidos/cliente/3
    @GetMapping("/cliente/{idCliente}")
    public List<LogPedidosEntity> buscarPorCliente(@PathVariable Integer idCliente) {
        return logPedidosService.buscarPorCliente(idCliente);
    }

    // GET /log-pedidos/operacao/INSERT
    @GetMapping("/operacao/{operacao}")
    public List<LogPedidosEntity> buscarPorOperacao(@PathVariable String operacao) {
        return logPedidosService.buscarPorOperacao(operacao);
    }
}