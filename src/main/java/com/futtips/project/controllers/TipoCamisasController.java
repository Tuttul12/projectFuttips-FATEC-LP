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

import com.futtips.project.entities.TipoCamisasEntity;
import com.futtips.project.services.TipoCamisasService;

@RestController
@RequestMapping("/tipo-camisas")
public class TipoCamisasController {

    @Autowired
    private TipoCamisasService tipoCamisasService;

    @GetMapping
    public List<TipoCamisasEntity> buscarTodos() {
        return tipoCamisasService.buscarTodos();
    }

    @GetMapping("/{id}")
    public Optional<TipoCamisasEntity> buscarPorId(@PathVariable Integer id) {
        return tipoCamisasService.buscarPorId(id);
    }

    // GET /tipo-camisas/fabricante/Nike
    @GetMapping("/fabricante/{fabricante}")
    public List<TipoCamisasEntity> buscarPorFabricante(@PathVariable String fabricante) {
        return tipoCamisasService.buscarPorFabricante(fabricante);
    }

    // GET /tipo-camisas/modelo/Camisa Home
    @GetMapping("/modelo/{modelo}")
    public List<TipoCamisasEntity> buscarPorModelo(@PathVariable String modelo) {
        return tipoCamisasService.buscarPorModelo(modelo);
    }

    @PostMapping
    public TipoCamisasEntity criar(@RequestBody TipoCamisasEntity tipo) {
        return tipoCamisasService.criar(tipo);
    }

    @PutMapping("/{id}")
    public TipoCamisasEntity editar(@PathVariable Integer id, @RequestBody TipoCamisasEntity tipo) {
        return tipoCamisasService.editar(id, tipo);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Integer id) {
        tipoCamisasService.excluir(id);
    }
}
