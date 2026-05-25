package com.futtips.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futtips.project.entities.ItensPedidosEntity;
import com.futtips.project.entities.dto.AtualizarItensPedidoDTO;
import com.futtips.project.responses.ApiResponse;
import com.futtips.project.services.ItensPedidosService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/itens-pedidos")
public class ItensPedidosController {

    @Autowired
    private ItensPedidosService itensPedidosService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ItensPedidosEntity>>> buscarTodos() {
        return ResponseEntity.ok(ApiResponse.sucesso("Itens de pedido encontrados com sucesso.", itensPedidosService.buscarTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItensPedidosEntity>> buscarPorId(@PathVariable Integer id) {
        return itensPedidosService.buscarPorId(id)
            .map(item -> ResponseEntity.ok(ApiResponse.sucesso("Item de pedido encontrado com sucesso.", item)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<ItensPedidosEntity>erro("Item de pedido não encontrado.", null)));
    }

    @GetMapping("/pedido/{pedidoCodigo}")
    public ResponseEntity<ApiResponse<List<ItensPedidosEntity>>> buscarPorPedido(@PathVariable Integer pedidoCodigo) {
        return ResponseEntity.ok(ApiResponse.sucesso("Itens encontrados por pedido.", itensPedidosService.buscarPorPedido(pedidoCodigo)));
    }

    @GetMapping("/camisa/{idCamisa}")
    public ResponseEntity<ApiResponse<List<ItensPedidosEntity>>> buscarPorCamisa(@PathVariable Integer idCamisa) {
        return ResponseEntity.ok(ApiResponse.sucesso("Itens encontrados por camisa.", itensPedidosService.buscarPorCamisa(idCamisa)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ItensPedidosEntity>> criar(@Valid @RequestBody ItensPedidosEntity item) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.sucesso("Item de pedido cadastrado com sucesso.", itensPedidosService.criar(item)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> excluir(@PathVariable Integer id) {
        itensPedidosService.excluir(id);
        return ResponseEntity.ok(ApiResponse.sucesso("Item de pedido excluído com sucesso.", null));
    }

    @PutMapping("/atualizar")
    public ResponseEntity<ApiResponse<List<ItensPedidosEntity>>> atualizar(@Valid @RequestBody AtualizarItensPedidoDTO dto) {
        return ResponseEntity.ok(ApiResponse.sucesso("Itens de pedido atualizados com sucesso.", itensPedidosService.atualizar(dto)));
    }
}
