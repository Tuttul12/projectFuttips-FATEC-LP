package com.futtips.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futtips.project.repositories.ClienteFunctionRepository;

@Service
public class ClienteFunctionService {

    @Autowired
    private ClienteFunctionRepository repository;

    public Double totalGasto(Integer idCliente) {
        return repository.totalGastoCliente(idCliente);
    }
}