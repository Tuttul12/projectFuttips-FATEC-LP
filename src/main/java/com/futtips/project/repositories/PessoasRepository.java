package com.futtips.project.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.futtips.project.entities.PessoasEntity;

public interface PessoasRepository extends JpaRepository <PessoasEntity, Integer> {

    Optional<PessoasEntity> findByEmail(String email);
}
