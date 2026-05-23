package com.futtips.project.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futtips.project.entities.PessoasEntity;
import com.futtips.project.services.PessoasService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping(value = "/pessoas")
public class PessoasController {

    @Autowired
    private PessoasService pessoasService;

    @GetMapping
    public List<PessoasEntity> buscarTodos(){
        return pessoasService.buscarTodos();
    }

    @GetMapping("/{id}")
    public Optional<PessoasEntity> buscarPessoas(@PathVariable Integer id){
        return pessoasService.buscarPessoas(id);
    }

    @PostMapping
    public PessoasEntity criar(@Valid @RequestBody PessoasEntity pessoasEntity) {
        return pessoasService.criar(pessoasEntity);
    }
    
    @PutMapping("/{id}")
    public PessoasEntity editar(@PathVariable int id,@RequestBody PessoasEntity pessoasEntity){
        return pessoasService.editar(id, pessoasEntity);
    }

    @PutMapping("/{id}/ativar")
    public PessoasEntity ativar(@PathVariable Integer id){
        return pessoasService.ativar(id);
    }

    @PutMapping("/{id}/desativar")
    public PessoasEntity desativar(@PathVariable Integer id){
        return pessoasService.desativar(id);
    }

    @DeleteMapping("/{id}")
    public PessoasEntity excluir(@PathVariable Integer id){
        return pessoasService.exluir(id);
    }

}
