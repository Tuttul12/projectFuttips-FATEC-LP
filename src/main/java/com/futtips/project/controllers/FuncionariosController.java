package com.futtips.project.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.futtips.project.entities.FuncionariosEntity;
import com.futtips.project.services.FuncionariosService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



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

    @PostMapping
    public FuncionariosEntity criar(@RequestBody FuncionariosEntity pessoasEntity) {
        return funcionariosService.criar(pessoasEntity);
    }
    
    @PutMapping("/{id}")
    public FuncionariosEntity editar(@PathVariable int id,@RequestBody FuncionariosEntity pessoasEntity){
        return funcionariosService.editar(id, pessoasEntity);
    }

    @DeleteMapping("/{id}")
    public FuncionariosEntity excluir(@PathVariable Integer id){
        return funcionariosService.exluir(id);
    }
    
    
}
