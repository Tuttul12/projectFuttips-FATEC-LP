package com.futtips.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futtips.project.entities.dto.RelatorioClienteDTO;
import com.futtips.project.repositories.RelatorioClientesRepository;

@Service
public class RelatorioClientesService {

    @Autowired
    private RelatorioClientesRepository repository;

    public List<RelatorioClienteDTO> listar() {
        return repository.listarRelatorio();
    }
}
