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

import com.futtips.project.entities.ClientesEntity;
import com.futtips.project.entities.dto.CriarClienteDTO;
import com.futtips.project.entities.dto.EditarClienteDTO;
import com.futtips.project.entities.dto.FuncionarioParaClienteDTO;
import com.futtips.project.responses.ApiResponse;
import com.futtips.project.services.ClientesService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClientesController {

    @Autowired
    private ClientesService clientesService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientesEntity>>> buscarTodos(){
        return ResponseEntity.ok(ApiResponse.sucesso("Clientes encontrados com sucesso.", clientesService.buscarTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientesEntity>> buscarClientes(@PathVariable Integer id){
        return clientesService.buscarClientes(id)
            .map(cliente -> ResponseEntity.ok(ApiResponse.sucesso("Cliente encontrado com sucesso.", cliente)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<ClientesEntity>erro("Cliente não encontrado.", null)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClientesEntity>> criar(@Valid @RequestBody CriarClienteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.sucesso("Cliente cadastrado com sucesso.", clientesService.criar(dto)));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientesEntity>> editar(
            @PathVariable int id,
            @Valid @RequestBody EditarClienteDTO dto) {

        ClientesEntity atualizado = clientesService.editar(id, dto);

        if (atualizado == null) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<ClientesEntity>erro("Cliente não encontrado.", null));
        }

        return ResponseEntity.ok(
            ApiResponse.sucesso("Cliente atualizado com sucesso.", atualizado)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientesEntity>> excluir(@PathVariable Integer id){
        return ResponseEntity.ok(ApiResponse.sucesso("Cliente excluído com sucesso.", clientesService.exluir(id)));
    }

    @PostMapping("/converter/funcionario-para-cliente")
    public ResponseEntity<ApiResponse<ClientesEntity>> funcionarioParaCliente(@Valid @RequestBody FuncionarioParaClienteDTO dto) {
        return ResponseEntity.ok(ApiResponse.sucesso("Funcionário convertido para cliente com sucesso.", clientesService.funcionarioParaCliente(dto)));
    }
}
