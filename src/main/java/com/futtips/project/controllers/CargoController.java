package com.futtips.project.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.futtips.project.entities.CargoEntity;
import com.futtips.project.entities.FuncionariosEntity;
import com.futtips.project.services.CargoService;




@RestController
@RequestMapping(value = "/cargo")
public class CargoController {

    @Autowired
    private CargoService cargoService;

    @GetMapping
    public List<CargoEntity> buscarTodos(){
        return cargoService.buscarTodos();
    }

    @GetMapping("/{id}")
    public Optional<CargoEntity> buscarCargo(@PathVariable Integer id){
        return cargoService.buscarCargo(id);
    }

    @GetMapping("/{id}/funcionarios")
    public List<FuncionariosEntity> buscarFuncionariosPorCargo(@PathVariable Integer id) {
        return cargoService.buscarFuncionariosPorCargo(id);
    }

    @PostMapping
    public CargoEntity criar(@RequestBody CargoEntity cargoEntity) {
        return cargoService.criar(cargoEntity);
    }
    
    @PutMapping("/{id}")
    public CargoEntity editar(@PathVariable int id,@RequestBody CargoEntity cargoEntity){
        return cargoService.editar(id, cargoEntity);
    }

    @DeleteMapping("/{id}")
    public CargoEntity excluir(@PathVariable Integer id){
        return cargoService.exluir(id);
    }
    
}
