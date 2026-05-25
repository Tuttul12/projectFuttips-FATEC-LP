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

import com.futtips.project.entities.PessoasEntity;
import com.futtips.project.responses.ApiResponse;
import com.futtips.project.services.PessoasService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/pessoas")
public class PessoasController {

    @Autowired
    private PessoasService pessoasService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PessoasEntity>>> buscarTodos(){
        return ResponseEntity.ok(ApiResponse.sucesso("Pessoas encontradas com sucesso.", pessoasService.buscarTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PessoasEntity>> buscarPessoas(@PathVariable Integer id){
        return pessoasService.buscarPessoas(id)
            .map(pessoa -> ResponseEntity.ok(ApiResponse.sucesso("Pessoa encontrada com sucesso.", pessoa)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<PessoasEntity>erro("Pessoa não encontrada.", null)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PessoasEntity>> criar(@Valid @RequestBody PessoasEntity pessoasEntity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.sucesso("Pessoa cadastrada com sucesso.", pessoasService.criar(pessoasEntity)));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PessoasEntity>> editar(@PathVariable int id, @Valid @RequestBody PessoasEntity pessoasEntity){
        PessoasEntity atualizado = pessoasService.editar(id, pessoasEntity);
        if (atualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<PessoasEntity>erro("Pessoa não encontrada.", null));
        }
        return ResponseEntity.ok(ApiResponse.sucesso("Pessoa atualizada com sucesso.", atualizado));
    }

    @PutMapping("/{id}/ativar")
    public ResponseEntity<ApiResponse<PessoasEntity>> ativar(@PathVariable Integer id){
        return ResponseEntity.ok(ApiResponse.sucesso("Pessoa ativada com sucesso.", pessoasService.ativar(id)));
    }

    @PutMapping("/{id}/desativar")
    public ResponseEntity<ApiResponse<PessoasEntity>> desativar(@PathVariable Integer id){
        return ResponseEntity.ok(ApiResponse.sucesso("Pessoa desativada com sucesso.", pessoasService.desativar(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<PessoasEntity>> excluir(@PathVariable Integer id){
        return ResponseEntity.ok(ApiResponse.sucesso("Pessoa excluída com sucesso.", pessoasService.exluir(id)));
    }
}
