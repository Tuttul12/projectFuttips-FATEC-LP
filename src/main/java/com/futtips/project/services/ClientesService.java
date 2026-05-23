package com.futtips.project.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futtips.project.entities.ClientesEntity;
import com.futtips.project.repositories.ClientesRepository;

@Service
public class ClientesService {

    @Autowired
    private ClientesRepository clientesRepository;
    
    public List<ClientesEntity> buscarTodos(){
        return clientesRepository.findAll();
    }

    public Optional<ClientesEntity> buscarClientes(Integer id){
        return clientesRepository.findById(id);
    }

    public ClientesEntity criar(ClientesEntity clientesEntity) {
        return clientesRepository.save(clientesEntity);
    }

    public ClientesEntity editar(int id, ClientesEntity clientesEntity) {
        Optional<ClientesEntity> clientes = clientesRepository.findById(id);
        if(clientes.isPresent()){
            ClientesEntity clientesParaAtualizar = clientes.get();
            clientesParaAtualizar.setNascimento(clientesEntity.getNascimento());
            clientesParaAtualizar.setTelefone(clientesEntity.getTelefone());
            clientesParaAtualizar.setDataCadastro(clientesEntity.getDataCadastro());
            return clientesRepository.save(clientesParaAtualizar);
        } else{
            return null;
        }      
    
    }

    public ClientesEntity exluir(Integer id) {
       ClientesEntity clientes = clientesRepository.findById(id).orElseThrow(() -> new RuntimeException("Pessoa não encontrado!"));
    clientesRepository.deleteById(id);
       return clientes;
    }
}
