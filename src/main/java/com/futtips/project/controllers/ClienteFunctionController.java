package com.futtips.project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.futtips.project.services.ClienteFunctionService;

@RestController
@RequestMapping("/clientes")
public class ClienteFunctionController {

    @Autowired
    private ClienteFunctionService service;

    @GetMapping("/{id}/total-gasto")
    public Double totalGasto(@PathVariable Integer id) {
        return service.totalGasto(id);
    }
}