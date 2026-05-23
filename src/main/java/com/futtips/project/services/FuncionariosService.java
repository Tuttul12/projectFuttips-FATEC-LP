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

    public FuncionariosEntity criar(FuncionariosEntity funcionariosEntity) {
        return funcionariosRepository.save(funcionariosEntity);
    }

    public FuncionariosEntity editar(int id, FuncionariosEntity funcionariosEntity) {
        Optional<FuncionariosEntity> funcionarios = funcionariosRepository.findById(id);
        if(funcionarios.isPresent()){
            FuncionariosEntity funcionariosParaAtualizar = funcionarios.get();
            funcionariosParaAtualizar.setSalario(funcionariosEntity.getSalario());
            funcionariosParaAtualizar.setCargo(funcionariosEntity.getCargo());
            return funcionariosRepository.save(funcionariosParaAtualizar);
        } else{
            return null;
        }      
    
    }

    public FuncionariosEntity exluir(Integer id) {
       FuncionariosEntity funcionarios = funcionariosRepository.findById(id).orElseThrow(() -> new RuntimeException("Funcionario não encontrado!"));
       funcionariosRepository.deleteById(id);
       return funcionarios;
    }

}
