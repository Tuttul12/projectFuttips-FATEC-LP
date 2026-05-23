package com.futtips.project.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.futtips.project.entities.EnderecosEntity;
import com.futtips.project.entities.pk.EnderecoPK;

public interface EnderecosRepository extends JpaRepository<EnderecosEntity, EnderecoPK> {

    List<EnderecosEntity> findByIdPessoasEntityId(Integer pessoaId);

    
    Optional<EnderecosEntity> findByIdIdEnderecoAndIdPessoasEntityId(Integer idEndereco, Integer pessoaId);

    void deleteByIdIdEnderecoAndIdPessoasEntityId(Integer idEndereco, Integer pessoaId);
}
