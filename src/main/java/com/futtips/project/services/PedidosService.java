package com.futtips.project.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futtips.project.entities.PedidosEntity;
import com.futtips.project.entities.dto.CriarPedidoDTO;
import com.futtips.project.repositories.PedidosRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class PedidosService {

    @Autowired
    private PedidosRepository pedidosRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<PedidosEntity> buscarTodos() {
        return pedidosRepository.findAll();
    }

    public Optional<PedidosEntity> buscarPedido(Integer id) {
        return pedidosRepository.findById(id);
    }

    public List<PedidosEntity> buscarPorCliente(Integer clienteId) {
        return pedidosRepository.findByClientesEntityId(clienteId);
    }

    @Transactional
    public PedidosEntity criar(CriarPedidoDTO dto) {

        // Converte a lista de itens para JSON string
        StringBuilder itensJson = new StringBuilder("[");
        for (int i = 0; i < dto.getItens().size(); i++) {
            CriarPedidoDTO.ItemPedidoDTO item = dto.getItens().get(i);
            itensJson.append("{\"idCamisa\":")
                    .append(item.getIdCamisa())
                    .append(",\"qtd\":")
                    .append(item.getQtd())
                    .append("}");
            if (i < dto.getItens().size() - 1) itensJson.append(",");
        }
        itensJson.append("]");

        // Chama a procedure
        entityManager.createNativeQuery(
            "EXEC sp_criar_pedido " +
            "@id_cliente = :idCliente, " +
            "@valor = :valor, " +
            "@itens = :itens")
            .setParameter("idCliente", dto.getIdCliente())
            .setParameter("valor",     dto.getValor())
            .setParameter("itens",     itensJson.toString())
            .executeUpdate();


        return pedidosRepository.findByClientesEntityId(dto.getIdCliente())
            .stream()
            .reduce((first, second) -> second)  // pega o último da lista
            .orElseThrow(() -> new RuntimeException("Erro ao buscar pedido após cadastro"));
    }

    public void excluir(Integer id) {
        pedidosRepository.deleteById(id);
    }

}
