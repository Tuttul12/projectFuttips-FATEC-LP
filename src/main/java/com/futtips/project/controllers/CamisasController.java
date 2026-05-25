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

import com.futtips.project.entities.CamisasEntity;
import com.futtips.project.responses.ApiResponse;
import com.futtips.project.services.CamisasService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/camisas")
public class CamisasController {

    @Autowired
    private CamisasService camisasService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CamisasEntity>>> buscarTodos() {
        return ResponseEntity.ok(ApiResponse.sucesso("Camisas encontradas com sucesso.", camisasService.buscarTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CamisasEntity>> buscarPorId(@PathVariable Integer id) {
        return camisasService.buscarPorId(id)
            .map(camisa -> ResponseEntity.ok(ApiResponse.sucesso("Camisa encontrada com sucesso.", camisa)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<CamisasEntity>erro("Camisa não encontrada.", null)));
    }

    @GetMapping("/tipo/{idTipo}")
    public ResponseEntity<ApiResponse<List<CamisasEntity>>> buscarPorTipo(@PathVariable Integer idTipo) {
        return ResponseEntity.ok(ApiResponse.sucesso("Camisas encontradas por tipo.", camisasService.buscarPorTipo(idTipo)));
    }

    @GetMapping("/funcionario/{idFuncionario}")
    public ResponseEntity<ApiResponse<List<CamisasEntity>>> buscarPorFuncionario(@PathVariable Integer idFuncionario) {
        return ResponseEntity.ok(ApiResponse.sucesso("Camisas encontradas por funcionário.", camisasService.buscarPorFuncionario(idFuncionario)));
    }

    @GetMapping("/tamanho/{tamanho}")
    public ResponseEntity<ApiResponse<List<CamisasEntity>>> buscarPorTamanho(@PathVariable String tamanho) {
        return ResponseEntity.ok(ApiResponse.sucesso("Camisas encontradas por tamanho.", camisasService.buscarPorTamanho(tamanho)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CamisasEntity>> criar(@Valid @RequestBody CamisasEntity camisa) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.sucesso("Camisa cadastrada com sucesso.", camisasService.criar(camisa)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CamisasEntity>> editar(@PathVariable Integer id, @Valid @RequestBody CamisasEntity camisa) {
        return ResponseEntity.ok(ApiResponse.sucesso("Camisa atualizada com sucesso.", camisasService.editar(id, camisa)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> excluir(@PathVariable Integer id) {
        camisasService.excluir(id);
        return ResponseEntity.ok(ApiResponse.sucesso("Camisa excluída com sucesso.", null));
    }
}
