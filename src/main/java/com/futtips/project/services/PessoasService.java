package com.futtips.project.services;

import java.util.List;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futtips.project.entities.PessoasEntity;
import com.futtips.project.repositories.PessoasRepository;


@Service
public class PessoasService {
    @Autowired
    private PessoasRepository pessoasRepository;
    
    public List<PessoasEntity> buscarTodos(){
        return pessoasRepository.findAll();
    }

    public Optional<PessoasEntity> buscarPessoas(Integer id){
        return pessoasRepository.findById(id);
    }


}
