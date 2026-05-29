package com.futtips.project.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futtips.project.entities.CamisasEntity;
import com.futtips.project.entities.ClientesEntity;
import com.futtips.project.entities.ItensPedidosEntity;
import com.futtips.project.entities.PedidosEntity;
import com.futtips.project.entities.dto.CriarPedidoDTO;
import com.futtips.project.repositories.CamisasRepository;
import com.futtips.project.repositories.ClientesRepository;
import com.futtips.project.repositories.ItensPedidosRepository;
import com.futtips.project.repositories.PedidosRepository;

import jakarta.transaction.Transactional;

@Service
public class PedidosService {

    @Autowired
    private PedidosRepository pedidosRepository;

    @Autowired
    private ClientesRepository clientesRepository;

    @Autowired
    private CamisasRepository camisasRepository;

    @Autowired
    private ItensPedidosRepository itensPedidosRepository;

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
        validarDtoPedido(dto);

        ClientesEntity cliente = clientesRepository.findById(dto.getIdCliente())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

        if (cliente.getAtivo() != null && !cliente.getAtivo()) {
            throw new RuntimeException("Não é possível criar pedido para cliente desativado!");
        }

        Map<Integer, Integer> quantidadesSolicitadas = agruparQuantidades(dto.getItens());
        Map<Integer, CamisasEntity> camisas = buscarEValidarEstoque(quantidadesSolicitadas);

        PedidosEntity pedido = new PedidosEntity();
        pedido.setClientesEntity(cliente);
        pedido.setValor(dto.getValor());
        pedido.setDataPedido(new Date());
        pedido.setProtocolo(gerarProtocolo());
        PedidosEntity pedidoSalvo = pedidosRepository.save(pedido);

        for (Map.Entry<Integer, Integer> entrada : quantidadesSolicitadas.entrySet()) {
            CamisasEntity camisa = camisas.get(entrada.getKey());
            Integer quantidadeVendida = entrada.getValue();

            ItensPedidosEntity item = new ItensPedidosEntity();
            item.setPedido(pedidoSalvo);
            item.setCamisa(camisa);
            item.setQtd(quantidadeVendida);
            itensPedidosRepository.save(item);

            camisa.setQuantidade(camisa.getQuantidade() - quantidadeVendida);
            camisasRepository.save(camisa);
        }

        return pedidoSalvo;
    }

    public void excluir(Integer id) {
        pedidosRepository.deleteById(id);
    }

    @Transactional
    public PedidosEntity editar(Integer id, CriarPedidoDTO dto) {
        validarDtoPedido(dto);

        PedidosEntity existente = pedidosRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));

        ClientesEntity cliente = clientesRepository.findById(dto.getIdCliente())
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));

        if (cliente.getAtivo() != null && !cliente.getAtivo()) {
            throw new RuntimeException("Não é possível associar pedido a cliente desativado!");
        }

        // Devolver estoque antigo e remover itens antigos
        if (existente.getItensPedidosEntity() != null) {
            for (ItensPedidosEntity itemAntigo : existente.getItensPedidosEntity()) {
                CamisasEntity camisa = itemAntigo.getCamisa();
                if (camisa != null) {
                    camisa.setQuantidade(camisa.getQuantidade() + itemAntigo.getQtd());
                    camisasRepository.save(camisa);
                }
                itensPedidosRepository.delete(itemAntigo);
            }
            existente.getItensPedidosEntity().clear();
        }

        // Validar e agrupar novas quantidades
        Map<Integer, Integer> quantidadesSolicitadas = agruparQuantidades(dto.getItens());
        Map<Integer, CamisasEntity> camisas = buscarEValidarEstoque(quantidadesSolicitadas);

        // Atualizar dados gerais do pedido
        existente.setClientesEntity(cliente);
        existente.setValor(dto.getValor());
        PedidosEntity pedidoAtualizado = pedidosRepository.save(existente);

        // Salvar novos itens e deduzir novo estoque
        for (Map.Entry<Integer, Integer> entrada : quantidadesSolicitadas.entrySet()) {
            CamisasEntity camisa = camisas.get(entrada.getKey());
            Integer quantidadeVendida = entrada.getValue();

            ItensPedidosEntity item = new ItensPedidosEntity();
            item.setPedido(pedidoAtualizado);
            item.setCamisa(camisa);
            item.setQtd(quantidadeVendida);
            itensPedidosRepository.save(item);

            camisa.setQuantidade(camisa.getQuantidade() - quantidadeVendida);
            camisasRepository.save(camisa);
        }

        return pedidoAtualizado;
    }

    private void validarDtoPedido(CriarPedidoDTO dto) {
        if (dto == null) {
            throw new RuntimeException("Dados do pedido não informados!");
        }
        if (dto.getIdCliente() == null) {
            throw new RuntimeException("Cliente do pedido não informado!");
        }
        if (dto.getValor() == null) {
            throw new RuntimeException("Valor do pedido não informado!");
        }
        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new RuntimeException("O pedido precisa ter pelo menos um item!");
        }
    }

    private Map<Integer, Integer> agruparQuantidades(List<CriarPedidoDTO.ItemPedidoDTO> itens) {
        Map<Integer, Integer> quantidades = new HashMap<>();

        for (CriarPedidoDTO.ItemPedidoDTO item : itens) {
            if (item.getIdCamisa() == null) {
                throw new RuntimeException("Camisa do item não informada!");
            }
            if (item.getQtd() == null || item.getQtd() <= 0) {
                throw new RuntimeException("A quantidade do item deve ser maior que zero!");
            }
            quantidades.merge(item.getIdCamisa(), item.getQtd(), Integer::sum);
        }

        return quantidades;
    }

    private Map<Integer, CamisasEntity> buscarEValidarEstoque(Map<Integer, Integer> quantidadesSolicitadas) {
        Map<Integer, CamisasEntity> camisas = new HashMap<>();

        for (Map.Entry<Integer, Integer> entrada : quantidadesSolicitadas.entrySet()) {
            CamisasEntity camisa = camisasRepository.findById(entrada.getKey())
                .orElseThrow(() -> new RuntimeException("Camisa não encontrada: id " + entrada.getKey()));

            Integer estoqueAtual = camisa.getQuantidade() == null ? 0 : camisa.getQuantidade();
            Integer quantidadeSolicitada = entrada.getValue();

            if (quantidadeSolicitada > estoqueAtual) {
                throw new RuntimeException(
                    "Estoque insuficiente para a camisa " + camisa.getIdCamisa() +
                    ". Estoque atual: " + estoqueAtual +
                    ", quantidade solicitada: " + quantidadeSolicitada
                );
            }

            camisas.put(camisa.getIdCamisa(), camisa);
        }

        return camisas;
    }

    private String gerarProtocolo() {
        return "PED-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }
}
