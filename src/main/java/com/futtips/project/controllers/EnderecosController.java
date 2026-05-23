package com.futtips.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futtips.project.entities.EnderecosEntity;
import com.futtips.project.services.EnderecosService;

@RestController
@RequestMapping("/enderecos")
public class EnderecosController {

    @Autowired
    private EnderecosService enderecosService;

    @GetMapping
    public List<EnderecosEntity> buscarTodos(){
        return enderecosService.buscarTodos();
    }

    @GetMapping("/pessoa/{pessoaId}")
    public List<EnderecosEntity> buscarPorPessoa(@PathVariable Integer pessoaId){
        return enderecosService.buscarPorPessoa(pessoaId);
    }

     @PostMapping
    public EnderecosEntity criar(@RequestBody EnderecosEntity enderecosEntity) {
        return enderecosService.criar(enderecosEntity);
    }
    
    @PutMapping("/{id}/pessoa/{pessoaId}")
    public EnderecosEntity editar(@PathVariable int id, @PathVariable int pessoaId,@RequestBody EnderecosEntity enderecosEntity){
        return enderecosService.editar(id,pessoaId, enderecosEntity);
    }

    @DeleteMapping("/{id}/pessoa/{pessoaId}")
    public void excluir(@PathVariable Integer id, @PathVariable int pessoaId){
        enderecosService.excluir(id, pessoaId);
    }
}
