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
        return enderecosRepository.findByIdPessoasEntityId(pessoaId);
    }

    public EnderecosEntity criar(EnderecosEntity enderecosEntity) {
        return enderecosRepository.save(enderecosEntity);
    }

    public EnderecosEntity editar(Integer idEndereco, Integer pessoaId, EnderecosEntity enderecosEntity) {
        Optional<EnderecosEntity> enderecos = enderecosRepository.findByIdIdEnderecoAndIdPessoasEntityId(idEndereco, pessoaId);
        if(enderecos.isPresent()){
            EnderecosEntity enderecosParaAtualizar = enderecos.get();
            enderecosParaAtualizar.setRua(enderecosEntity.getRua());
            enderecosParaAtualizar.setNumero(enderecosEntity.getNumero());
            enderecosParaAtualizar.setBairro(enderecosEntity.getBairro());
            enderecosParaAtualizar.setCidade(enderecosEntity.getCidade());
            enderecosParaAtualizar.setEstado(enderecosEntity.getEstado());
            enderecosParaAtualizar.setCep(enderecosEntity.getCep());
            return enderecosRepository.save(enderecosParaAtualizar);
        } else{
            return null;
        }      
    
    }

    public void excluir(Integer idEndereco, Integer pessoaId) {
       enderecosRepository.deleteByIdIdEnderecoAndIdPessoasEntityId(idEndereco, pessoaId);
    }
}
