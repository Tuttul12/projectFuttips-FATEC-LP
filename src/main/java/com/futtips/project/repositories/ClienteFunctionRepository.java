package com.futtips.project.repositories;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Repository
public class ClienteFunctionRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Double totalGastoCliente(Integer idCliente) {

        return ((Number) entityManager
            .createNativeQuery(
                "SELECT dbo.fn_total_gasto_cliente(:idCliente)"
            )
            .setParameter("idCliente", idCliente)
            .getSingleResult()
        ).doubleValue();
    }
}
