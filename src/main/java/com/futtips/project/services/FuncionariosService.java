package com.futtips.project.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.futtips.project.entities.FuncionariosEntity;
import com.futtips.project.repositories.FuncionariosRepository;

@Service
public class FuncionariosService {

    @Autowired
    private FuncionariosRepository funcionariosRepository;

    public List<FuncionariosEntity> buscarTodos() {
        return funcionariosRepository.findAll();
    }

    public Optional<FuncionariosEntity> buscarFuncionario(Integer id) {
        return funcionariosRepository.findById(id);
    }

}
