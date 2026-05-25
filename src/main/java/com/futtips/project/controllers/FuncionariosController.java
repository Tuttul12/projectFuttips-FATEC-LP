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

import com.futtips.project.entities.FuncionariosEntity;
import com.futtips.project.entities.dto.ClienteParaFuncionarioDTO;
import com.futtips.project.entities.dto.CriarFuncionarioDTO;
import com.futtips.project.responses.ApiResponse;
import com.futtips.project.services.FuncionariosService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value= "/funcionarios")
public class FuncionariosController {

    @Autowired
    private FuncionariosService funcionariosService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<FuncionariosEntity>>> buscarTodos(){
        return ResponseEntity.ok(ApiResponse.sucesso("Funcionários encontrados com sucesso.", funcionariosService.buscarTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FuncionariosEntity>> buscarFuncionario(@PathVariable Integer id){
        return funcionariosService.buscarFuncionario(id)
            .map(funcionario -> ResponseEntity.ok(ApiResponse.sucesso("Funcionário encontrado com sucesso.", funcionario)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<FuncionariosEntity>erro("Funcionário não encontrado.", null)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FuncionariosEntity>> criar(@Valid @RequestBody CriarFuncionarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.sucesso("Funcionário cadastrado com sucesso.", funcionariosService.criar(dto)));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FuncionariosEntity>> editar(@PathVariable int id, @Valid @RequestBody FuncionariosEntity funcionariosEntity){
        FuncionariosEntity atualizado = funcionariosService.editar(id, funcionariosEntity);
        if (atualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<FuncionariosEntity>erro("Funcionário não encontrado.", null));
        }
        return ResponseEntity.ok(ApiResponse.sucesso("Funcionário atualizado com sucesso.", atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<FuncionariosEntity>> excluir(@PathVariable Integer id){
        return ResponseEntity.ok(ApiResponse.sucesso("Funcionário excluído com sucesso.", funcionariosService.exluir(id)));
    }

    @PostMapping("/converter/cliente-para-funcionario")
    public ResponseEntity<ApiResponse<FuncionariosEntity>> clienteParaFuncionario(@Valid @RequestBody ClienteParaFuncionarioDTO dto) {
        return ResponseEntity.ok(ApiResponse.sucesso("Cliente convertido para funcionário com sucesso.", funcionariosService.clienteParaFuncionario(dto)));
    }
}
