package com.futtips.project.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futtips.project.entities.CargoEntity;
import com.futtips.project.entities.FuncionariosEntity;
import com.futtips.project.repositories.CargoRepository;
import com.futtips.project.repositories.FuncionariosRepository;

@Service
public class CargoService {

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private FuncionariosRepository funcionariosRepository;

    public List<CargoEntity> buscarTodos() {
        return cargoRepository.findAll();
    }

    public Optional<CargoEntity> buscarCargo(Integer id) {
        return cargoRepository.findById(id);
    }

    public List<FuncionariosEntity> buscarFuncionariosPorCargo(Integer codigoCargo) {
        return funcionariosRepository.findByCargoCodigo(codigoCargo);
    }


}
