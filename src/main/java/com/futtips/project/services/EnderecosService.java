package com.futtips.project.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futtips.project.entities.EnderecosEntity;
import com.futtips.project.entities.pk.EnderecoPK;
import com.futtips.project.repositories.EnderecosRepository;

@Service
public class EnderecosService {

    @Autowired
    private EnderecosRepository enderecosRepository;

    public List<EnderecosEntity> buscarTodos() {
        return enderecosRepository.findAll();
    }

    public Optional<EnderecosEntity> buscarPorId(EnderecoPK id) {
        return enderecosRepository.findById(id);
    }

    public List<EnderecosEntity> buscarPorPessoa(Integer pessoaId) {
        return enderecosRepository.findByIdPessoaId(pessoaId);
    }
}
