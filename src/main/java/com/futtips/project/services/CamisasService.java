package com.futtips.project.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futtips.project.entities.CamisasEntity;
import com.futtips.project.repositories.CamisasRepository;

@Service
public class CamisasService {

    @Autowired
    private CamisasRepository camisasRepository;

    public List<CamisasEntity> buscarTodos() {
        return camisasRepository.findAll();
    }

    public Optional<CamisasEntity> buscarPorId(Integer id) {
        return camisasRepository.findById(id);
    }

    public List<CamisasEntity> buscarPorTipo(Integer idTipo) {
        return camisasRepository.findByTipoCamisasEntityIdTipo(idTipo);
    }

    public List<CamisasEntity> buscarPorFuncionario(Integer idFuncionario) {
        return camisasRepository.findByFuncionariosEntityId(idFuncionario); 
    }

    public List<CamisasEntity> buscarPorTamanho(String tamanho) {
        return camisasRepository.findByTamanho(tamanho);
    }

    public CamisasEntity criar(CamisasEntity camisa) {
        return camisasRepository.save(camisa);
    }

    public CamisasEntity editar(Integer id, CamisasEntity camisa) {
        CamisasEntity existente = camisasRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Camisa não encontrada!"));
        existente.setDescricao(camisa.getDescricao());
        existente.setTamanho(camisa.getTamanho());
        existente.setQuantidade(camisa.getQuantidade());
        return camisasRepository.save(existente);
    }

    public void excluir(Integer id) {
        camisasRepository.deleteById(id);
    }
}
