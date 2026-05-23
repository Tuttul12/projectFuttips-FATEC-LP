package com.futtips.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.futtips.project.entities.ClientesEntity;

public interface ClientesRepository extends JpaRepository <ClientesEntity, Integer> {

}
