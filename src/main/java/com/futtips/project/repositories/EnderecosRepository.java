package com.futtips.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.futtips.project.entities.EnderecosEntity;
import com.futtips.project.entities.pk.EnderecoPK;

public interface EnderecosRepository extends JpaRepository<EnderecosEntity, EnderecoPK> {

    List<EnderecosEntity> findByIdPessoaId(Integer pessoaId);
}
