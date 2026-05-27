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

import com.futtips.project.entities.TipoCamisasEntity;
import com.futtips.project.responses.ApiResponse;
import com.futtips.project.services.TipoCamisasService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tipo-camisas")
public class TipoCamisasController {

    @Autowired
    private TipoCamisasService tipoCamisasService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TipoCamisasEntity>>> buscarTodos() {
        return ResponseEntity.ok(ApiResponse.sucesso("Tipos de camisa encontrados com sucesso.", tipoCamisasService.buscarTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TipoCamisasEntity>> buscarPorId(@PathVariable Integer id) {
        return tipoCamisasService.buscarPorId(id)
            .map(tipo -> ResponseEntity.ok(ApiResponse.sucesso("Tipo de camisa encontrado com sucesso.", tipo)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<TipoCamisasEntity>erro("Tipo de camisa não encontrado.", null)));
    }

    @GetMapping("/fabricante/{fabricante}")
    public ResponseEntity<ApiResponse<List<TipoCamisasEntity>>> buscarPorFabricante(@PathVariable String fabricante) {
        return ResponseEntity.ok(ApiResponse.sucesso("Tipos de camisa encontrados por fabricante.", tipoCamisasService.buscarPorFabricante(fabricante)));
    }

    @GetMapping("/modelo/{modelo}")
    public ResponseEntity<ApiResponse<List<TipoCamisasEntity>>> buscarPorModelo(@PathVariable String modelo) {
        return ResponseEntity.ok(ApiResponse.sucesso("Tipos de camisa encontrados por modelo.", tipoCamisasService.buscarPorModelo(modelo)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TipoCamisasEntity>> criar(@Valid @RequestBody TipoCamisasEntity tipo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.sucesso("Tipo de camisa cadastrado com sucesso.", tipoCamisasService.criar(tipo)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TipoCamisasEntity>> editar(@PathVariable Integer id, @Valid @RequestBody TipoCamisasEntity tipo) {
        return ResponseEntity.ok(ApiResponse.sucesso("Tipo de camisa atualizado com sucesso.", tipoCamisasService.editar(id, tipo)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> excluir(@PathVariable Integer id) {
        tipoCamisasService.excluir(id);
        return ResponseEntity.ok(ApiResponse.sucesso("Tipo de camisa excluído com sucesso.", null));
    }
}
