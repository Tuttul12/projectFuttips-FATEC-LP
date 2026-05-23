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
        if (pessoasEntity.getAtivo() == null) {
            pessoasEntity.setAtivo(true);
        }
        return pessoasRepository.save(pessoasEntity);
    }

    public PessoasEntity editar(int id, PessoasEntity pessoasEntity) {
        Optional<PessoasEntity> pessoas = pessoasRepository.findById(id);
        if(pessoas.isPresent()){
            PessoasEntity pessoasParaAtualizar = pessoas.get();
            if (pessoasParaAtualizar.getAtivo() != null && !pessoasParaAtualizar.getAtivo()) {
                throw new RuntimeException("Não é possível editar uma pessoa desativada. Ative o usuário antes de alterar seus dados.");
            }
            pessoasParaAtualizar.setNome(pessoasEntity.getNome());
            pessoasParaAtualizar.setCpf(pessoasEntity.getCpf());
            pessoasParaAtualizar.setEmail(pessoasEntity.getEmail());
            pessoasParaAtualizar.setSenha(pessoasEntity.getSenha());
            return pessoasRepository.save(pessoasParaAtualizar);
        } else{
            return null;
        }      
    }

    public PessoasEntity ativar(Integer id) {
        PessoasEntity pessoa = pessoasRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pessoa não encontrada!"));

        if (Boolean.TRUE.equals(pessoa.getAtivo())) {
            throw new RuntimeException("Pessoa já está ativa!");
        }

        pessoa.setAtivo(true);
        return pessoasRepository.save(pessoa);
    }

    public PessoasEntity desativar(Integer id) {
        PessoasEntity pessoa = pessoasRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pessoa não encontrada!"));

        if (Boolean.FALSE.equals(pessoa.getAtivo())) {
            throw new RuntimeException("Pessoa já está desativada!");
        }

        pessoa.setAtivo(false);
        return pessoasRepository.save(pessoa);
    }

    public PessoasEntity exluir(Integer id) {
       PessoasEntity pessoas = pessoasRepository.findById(id).orElseThrow(() -> new RuntimeException("Pessoa não encontrado!"));
       pessoasRepository.deleteById(id);
       return pessoas;
    }
}
