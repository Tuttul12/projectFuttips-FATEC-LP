package com.futtips.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.futtips.project.entities.FuncionariosEntity;

public interface FuncionariosRepository extends JpaRepository <FuncionariosEntity, Integer> {

    List<FuncionariosEntity> findByCargoCodigo(Integer codigoCargo);

}