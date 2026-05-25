package com.futtips.project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.futtips.project.responses.ApiResponse;
import com.futtips.project.services.CargoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/cargo")
public class CargoController {

    @Autowired
    private CargoService cargoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CargoEntity>>> buscarTodos(){
        return ResponseEntity.ok(ApiResponse.sucesso("Cargos encontrados com sucesso.", cargoService.buscarTodos()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CargoEntity>> buscarCargo(@PathVariable Integer id){
        return cargoService.buscarCargo(id)
            .map(cargo -> ResponseEntity.ok(ApiResponse.sucesso("Cargo encontrado com sucesso.", cargo)))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<CargoEntity>erro("Cargo não encontrado.", null)));
    }

    @GetMapping("/{id}/funcionarios")
    public ResponseEntity<ApiResponse<List<FuncionariosEntity>>> buscarFuncionariosPorCargo(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.sucesso("Funcionários encontrados por cargo.", cargoService.buscarFuncionariosPorCargo(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CargoEntity>> criar(@Valid @RequestBody CargoEntity cargoEntity) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.sucesso("Cargo cadastrado com sucesso.", cargoService.criar(cargoEntity)));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CargoEntity>> editar(@PathVariable int id, @Valid @RequestBody CargoEntity cargoEntity){
        CargoEntity atualizado = cargoService.editar(id, cargoEntity);
        if (atualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.<CargoEntity>erro("Cargo não encontrado.", null));
        }
        return ResponseEntity.ok(ApiResponse.sucesso("Cargo atualizado com sucesso.", atualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<CargoEntity>> excluir(@PathVariable Integer id){
        return ResponseEntity.ok(ApiResponse.sucesso("Cargo excluído com sucesso.", cargoService.exluir(id)));
    }
}
