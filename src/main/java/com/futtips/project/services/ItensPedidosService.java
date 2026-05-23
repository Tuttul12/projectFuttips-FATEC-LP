package com.futtips.project.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futtips.project.entities.ItensPedidosEntity;
import com.futtips.project.entities.dto.AtualizarItensPedidoDTO;
import com.futtips.project.repositories.ItensPedidosRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class ItensPedidosService {

    @Autowired
    private ItensPedidosRepository itensPedidosRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<ItensPedidosEntity> atualizar(AtualizarItensPedidoDTO dto) {

        
        StringBuilder itensJson = new StringBuilder("[");
        for (int i = 0; i < dto.getItens().size(); i++) {
            AtualizarItensPedidoDTO.ItemPedidoDTO item = dto.getItens().get(i);
            itensJson.append("{\"idCamisa\":")
                    .append(item.getIdCamisa())
                    .append(",\"qtd\":")
                    .append(item.getQtd())
                    .append("}");
            if (i < dto.getItens().size() - 1) itensJson.append(",");
        }
        itensJson.append("]");

        
        entityManager.createNativeQuery(
            "EXEC sp_atualizar_itens_pedido " +
            "@id_pedido = :idPedido, " +
            "@itens = :itens")
            .setParameter("idPedido", dto.getIdPedido())
            .setParameter("itens",    itensJson.toString())
            .executeUpdate();

        
        return itensPedidosRepository.findByPedidoCodigo(dto.getIdPedido());
    }

    public List<ItensPedidosEntity> buscarTodos() {
        return itensPedidosRepository.findAll();
    }

    public Optional<ItensPedidosEntity> buscarPorId(Integer id) {
        return itensPedidosRepository.findById(id);
    }

    public List<ItensPedidosEntity> buscarPorPedido(Integer pedidoCodigo) {
        return itensPedidosRepository.findByPedidoCodigo(pedidoCodigo);
    }

    public List<ItensPedidosEntity> buscarPorCamisa(Integer idCamisa) {
        return itensPedidosRepository.findByCamisaIdCamisa(idCamisa);
    }

    public ItensPedidosEntity criar(ItensPedidosEntity item) {
        return itensPedidosRepository.save(item);
    }

    public void excluir(Integer id) {
        itensPedidosRepository.deleteById(id);
    }
}
