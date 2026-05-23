package com.futtips.project.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futtips.project.entities.TipoCamisasEntity;
import com.futtips.project.repositories.TipoCamisasRepository;

@Service
public class TipoCamisasService {

    @Autowired
    private TipoCamisasRepository tipoCamisasRepository;

    public List<TipoCamisasEntity> buscarTodos() {
        return tipoCamisasRepository.findAll();
    }

    public Optional<TipoCamisasEntity> buscarPorId(Integer id) {
        return tipoCamisasRepository.findById(id);
    }

    public List<TipoCamisasEntity> buscarPorFabricante(String fabricante) {
        return tipoCamisasRepository.buscaPorFabricante(fabricante);
    }

    public List<TipoCamisasEntity> buscarPorModelo(String modelo) {
        return tipoCamisasRepository.buscaPorModelo(modelo);
    }

    public TipoCamisasEntity criar(TipoCamisasEntity tipo) {
        return tipoCamisasRepository.save(tipo);
    }

    public TipoCamisasEntity editar(Integer id, TipoCamisasEntity tipo) {
        TipoCamisasEntity existente = tipoCamisasRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tipo de camisa não encontrado!"));
        existente.setModelo(tipo.getModelo());
        existente.setFabricante(tipo.getFabricante());
        return tipoCamisasRepository.save(existente);
    }

    public void excluir(Integer id) {
        tipoCamisasRepository.deleteById(id);
    }
}
