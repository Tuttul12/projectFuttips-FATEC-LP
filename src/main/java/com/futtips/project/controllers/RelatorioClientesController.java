package com.futtips.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futtips.project.entities.dto.RelatorioClienteDTO;
import com.futtips.project.services.RelatorioClientesService;

@RestController
@RequestMapping("/relatorios")
public class RelatorioClientesController {

    @Autowired
    private RelatorioClientesService service;

    @GetMapping
    public List<RelatorioClienteDTO> listar() {
        return service.listar();
    }
}