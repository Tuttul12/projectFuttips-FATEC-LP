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

import com.futtips.project.entities.EnderecosEntity;
import com.futtips.project.responses.ApiResponse;
import com.futtips.project.services.EnderecosService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/enderecos")
public class EnderecosController {

    @Autowired
    private EnderecosService enderecosService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EnderecosEntity>>> buscarTodos(){
        return ResponseEntity.ok(ApiResponse.sucesso("Endereços encontrados com sucesso.", enderecosService.buscarTodos()));
    }

    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<ApiResponse<List<EnderecosEntity>>> buscarPorPessoa(@PathVariable Integer pessoaId){
        return ResponseEntity.ok(ApiResponse.sucesso("Endereços encontrados por pessoa.", enderecosService.buscarPorPessoa(pessoaId)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EnderecosEntity>> criar(@Valid @RequestBody EnderecosEntity enderecosEntity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.sucesso("Endereço cadastrado com sucesso.", enderecosService.criar(enderecosEntity)));
    }
    
    @PutMapping("/{id}/pessoa/{pessoaId}")
    public ResponseEntity<ApiResponse<EnderecosEntity>> editar(@PathVariable int id, @PathVariable int pessoaId, @Valid @RequestBody EnderecosEntity enderecosEntity){
        EnderecosEntity atualizado = enderecosService.editar(id, pessoaId, enderecosEntity);
        if (atualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<EnderecosEntity>erro("Endereço não encontrado.", null));
        }
        return ResponseEntity.ok(ApiResponse.sucesso("Endereço atualizado com sucesso.", atualizado));
    }

    @DeleteMapping("/{id}/pessoa/{pessoaId}")
    public ResponseEntity<ApiResponse<Void>> excluir(@PathVariable Integer id, @PathVariable int pessoaId){
        enderecosService.excluir(id, pessoaId);
        return ResponseEntity.ok(ApiResponse.sucesso("Endereço excluído com sucesso.", null));
    }
}
