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

import com.futtips.project.entities.CamisasEntity;
import com.futtips.project.services.CamisasService;

@RestController
@RequestMapping("/camisas")
public class CamisasController {

    @Autowired
    private CamisasService camisasService;

    @GetMapping
    public List<CamisasEntity> buscarTodos() {
        return camisasService.buscarTodos();
    }

    @GetMapping("/{id}")
    public Optional<CamisasEntity> buscarPorId(@PathVariable Integer id) {
        return camisasService.buscarPorId(id);
    }

    
    @GetMapping("/tipo/{idTipo}")
    public List<CamisasEntity> buscarPorTipo(@PathVariable Integer idTipo) {
        return camisasService.buscarPorTipo(idTipo);
    }

    
    @GetMapping("/funcionario/{idFuncionario}")
    public List<CamisasEntity> buscarPorFuncionario(@PathVariable Integer idFuncionario) {
        return camisasService.buscarPorFuncionario(idFuncionario);
    }

    
    @GetMapping("/tamanho/{tamanho}")
    public List<CamisasEntity> buscarPorTamanho(@PathVariable String tamanho) {
        return camisasService.buscarPorTamanho(tamanho);
    }

    @PostMapping
    public CamisasEntity criar(@RequestBody CamisasEntity camisa) {
        return camisasService.criar(camisa);
    }

    @PutMapping("/{id}")
    public CamisasEntity editar(@PathVariable Integer id, @RequestBody CamisasEntity camisa) {
        return camisasService.editar(id, camisa);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Integer id) {
        camisasService.excluir(id);
    }
}
