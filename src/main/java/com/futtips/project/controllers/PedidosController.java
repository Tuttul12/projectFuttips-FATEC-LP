package com.futtips.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futtips.project.entities.PedidosEntity;
import com.futtips.project.entities.dto.CriarPedidoDTO;
import com.futtips.project.responses.ApiResponse;
import com.futtips.project.services.PedidosService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/pedidos")
public class PedidosController {
    @Autowired
    private PedidosService pedidosService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PedidosEntity>>> buscarTodos() {
        return ResponseEntity.ok(ApiResponse.sucesso("Pedidos encontrados com sucesso.", pedidosService.buscarTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PedidosEntity>> buscarPedido(@PathVariable Integer id) {
        return pedidosService.buscarPedido(id)
            .map(pedido -> ResponseEntity.ok(ApiResponse.sucesso("Pedido encontrado com sucesso.", pedido)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<PedidosEntity>erro("Pedido não encontrado.", null)));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<ApiResponse<List<PedidosEntity>>> buscarPorCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(ApiResponse.sucesso("Pedidos encontrados por cliente.", pedidosService.buscarPorCliente(clienteId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PedidosEntity>> criar(@Valid @RequestBody CriarPedidoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.sucesso("Pedido cadastrado com sucesso.", pedidosService.criar(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PedidosEntity>> editar(@PathVariable Integer id, @Valid @RequestBody CriarPedidoDTO dto) {
        return ResponseEntity.ok(ApiResponse.sucesso("Pedido atualizado com sucesso.", pedidosService.editar(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> excluir(@PathVariable Integer id) {
        pedidosService.excluir(id);
        return ResponseEntity.ok(ApiResponse.sucesso("Pedido excluído com sucesso.", null));
    }
}
