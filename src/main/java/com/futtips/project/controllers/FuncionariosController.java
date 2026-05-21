package com.futtips.project.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.futtips.project.entities.FuncionariosEntity;
import com.futtips.project.services.FuncionariosService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping(value= "/funcionarios")
public class FuncionariosController {

    @Autowired
    private FuncionariosService funcionariosService;

    @GetMapping
    public List<FuncionariosEntity> buscarTodos(){
        return funcionariosService.buscarTodos();
    }

    @GetMapping("/{id}")
    public Optional<FuncionariosEntity> buscarFuncionario(@PathVariable Integer id){
        return funcionariosService.buscarFuncionario(id);
    }
    
    
}
