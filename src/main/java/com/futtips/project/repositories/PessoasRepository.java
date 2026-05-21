package com.futtips.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.futtips.project.entities.PessoasEntity;

public interface PessoasRepository extends JpaRepository <PessoasEntity, Integer> {

}
