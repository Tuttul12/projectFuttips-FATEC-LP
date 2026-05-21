package com.futtips.project.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.futtips.project.entities.CargoEntity;

public interface CargoRepository extends JpaRepository <CargoEntity, Integer> {

    
}