package com.futtips.project.repositories;

import java.util.List;
import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.futtips.project.entities.dto.RelatorioClienteDTO;

@Repository
public class RelatorioClientesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<RelatorioClienteDTO> listarRelatorio() {

        return entityManager.createNativeQuery(
            "SELECT id_pessoa, nome_cliente, cpf, total_pedidos, valor_total_gasto " +
            "FROM vw_relatorio_clientes",
            "RelatorioClienteMapping"
        ).getResultList();
    }
}