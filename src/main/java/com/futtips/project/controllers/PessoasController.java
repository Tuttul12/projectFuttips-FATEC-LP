package com.futtips.project.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futtips.project.entities.PessoasEntity;
import com.futtips.project.services.PessoasService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



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
    
}
