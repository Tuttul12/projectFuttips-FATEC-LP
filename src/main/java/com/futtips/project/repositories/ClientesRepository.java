package com.futtips.project.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.futtips.project.entities.ClientesEntity;

public interface ClientesRepository extends JpaRepository <ClientesEntity, Integer> {

    Optional<ClientesEntity> findByCpf(String cpf);
}
