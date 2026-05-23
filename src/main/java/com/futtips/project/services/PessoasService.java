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

    public PessoasEntity criar(PessoasEntity pessoasEntity) {
        return pessoasRepository.save(pessoasEntity);
    }

    public PessoasEntity editar(int id, PessoasEntity pessoasEntity) {
        Optional<PessoasEntity> pessoas = pessoasRepository.findById(id);
        if(pessoas.isPresent()){
            PessoasEntity pessoasParaAtualizar = pessoas.get();
            pessoasParaAtualizar.setNome(pessoasEntity.getNome());
            pessoasParaAtualizar.setCpf(pessoasEntity.getCpf());
            pessoasParaAtualizar.setEmail(pessoasEntity.getEmail());
            pessoasParaAtualizar.setSenha(pessoasEntity.getSenha());
            return pessoasRepository.save(pessoasParaAtualizar);
        } else{
            return null;
        }      
    
    }

    public PessoasEntity exluir(Integer id) {
       PessoasEntity pessoas = pessoasRepository.findById(id).orElseThrow(() -> new RuntimeException("Pessoa não encontrado!"));
       pessoasRepository.deleteById(id);
       return pessoas;
    }

}
