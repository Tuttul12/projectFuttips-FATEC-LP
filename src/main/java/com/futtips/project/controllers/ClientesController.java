package com.futtips.project.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.futtips.project.entities.dto.FuncionarioParaClienteDTO;
import com.futtips.project.services.ClientesService;

@RestController
@RequestMapping("/clientes")
public class ClientesController {

    @Autowired
    private ClientesService clientesService;

    @GetMapping
    public List<ClientesEntity> buscarTodos(){
        return clientesService.buscarTodos();
    }

    @GetMapping("/{id}")
    public Optional<ClientesEntity> buscarClientes(@PathVariable Integer id){
        return clientesService.buscarClientes(id);
    }

    @PostMapping
    public ClientesEntity criar(@RequestBody CriarClienteDTO dto) {
        return clientesService.criar(dto);
    }
    
    @PutMapping("/{id}")
    public ClientesEntity editar(@PathVariable int id,@RequestBody ClientesEntity clientesEntity){
        return clientesService.editar(id, clientesEntity);
    }

    @DeleteMapping("/{id}")
    public ClientesEntity excluir(@PathVariable Integer id){
        return clientesService.exluir(id);
    }

    @PostMapping("/converter/funcionario-para-cliente")
    public ClientesEntity funcionarioParaCliente(@RequestBody FuncionarioParaClienteDTO dto) {
        return clientesService.funcionarioParaCliente(dto);
    }
}
