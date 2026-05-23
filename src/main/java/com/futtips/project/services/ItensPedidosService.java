package com.futtips.project.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.futtips.project.entities.CamisasEntity;
import com.futtips.project.entities.ItensPedidosEntity;
import com.futtips.project.entities.PedidosEntity;
import com.futtips.project.entities.dto.AtualizarItensPedidoDTO;
import com.futtips.project.repositories.CamisasRepository;
import com.futtips.project.repositories.ItensPedidosRepository;
import com.futtips.project.repositories.PedidosRepository;

import jakarta.transaction.Transactional;

@Service
public class ItensPedidosService {

    @Autowired
    private ItensPedidosRepository itensPedidosRepository;

    @Autowired
    private PedidosRepository pedidosRepository;

    @Autowired
    private CamisasRepository camisasRepository;

    @Transactional
    public List<ItensPedidosEntity> atualizar(AtualizarItensPedidoDTO dto) {
        validarDtoAtualizacao(dto);

        PedidosEntity pedido = pedidosRepository.findById(dto.getIdPedido())
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));

        List<ItensPedidosEntity> itensAtuais = itensPedidosRepository.findByPedidoCodigo(dto.getIdPedido());
        Map<Integer, Integer> quantidadesAtuais = agruparQuantidadesAtuais(itensAtuais);
        Map<Integer, Integer> novasQuantidades = agruparNovasQuantidades(dto.getItens());
        Map<Integer, CamisasEntity> camisas = buscarEValidarEstoqueParaAtualizacao(novasQuantidades, quantidadesAtuais);

        // Remove os itens antigos e devolve as quantidades deles ao estoque lógico.
        itensPedidosRepository.deleteByPedidoCodigo(dto.getIdPedido());

        for (Map.Entry<Integer, Integer> entrada : novasQuantidades.entrySet()) {
            CamisasEntity camisa = camisas.get(entrada.getKey());
            Integer quantidadeAnterior = quantidadesAtuais.getOrDefault(entrada.getKey(), 0);
            Integer quantidadeNova = entrada.getValue();

            camisa.setQuantidade(camisa.getQuantidade() + quantidadeAnterior - quantidadeNova);
            camisasRepository.save(camisa);

            ItensPedidosEntity novoItem = new ItensPedidosEntity();
            novoItem.setPedido(pedido);
            novoItem.setCamisa(camisa);
            novoItem.setQtd(quantidadeNova);
            itensPedidosRepository.save(novoItem);
        }

        // Camisas que estavam no pedido antigo e não estão no novo precisam receber a quantidade de volta.
        for (Map.Entry<Integer, Integer> entrada : quantidadesAtuais.entrySet()) {
            if (!novasQuantidades.containsKey(entrada.getKey())) {
                CamisasEntity camisa = camisasRepository.findById(entrada.getKey())
                    .orElseThrow(() -> new RuntimeException("Camisa não encontrada: id " + entrada.getKey()));
                camisa.setQuantidade(camisa.getQuantidade() + entrada.getValue());
                camisasRepository.save(camisa);
            }
        }

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

    @Transactional
    public ItensPedidosEntity criar(ItensPedidosEntity item) {
        if (item == null || item.getCamisa() == null || item.getCamisa().getIdCamisa() == null) {
            throw new RuntimeException("Camisa do item não informada!");
        }
        if (item.getPedido() == null || item.getPedido().getCodigo() == null) {
            throw new RuntimeException("Pedido do item não informado!");
        }
        if (item.getQtd() == null || item.getQtd() <= 0) {
            throw new RuntimeException("A quantidade do item deve ser maior que zero!");
        }

        PedidosEntity pedido = pedidosRepository.findById(item.getPedido().getCodigo())
            .orElseThrow(() -> new RuntimeException("Pedido não encontrado!"));

        CamisasEntity camisa = camisasRepository.findById(item.getCamisa().getIdCamisa())
            .orElseThrow(() -> new RuntimeException("Camisa não encontrada!"));

        Integer estoqueAtual = camisa.getQuantidade() == null ? 0 : camisa.getQuantidade();
        if (item.getQtd() > estoqueAtual) {
            throw new RuntimeException("Estoque insuficiente para a camisa " + camisa.getIdCamisa() +
                ". Estoque atual: " + estoqueAtual + ", quantidade solicitada: " + item.getQtd());
        }

        camisa.setQuantidade(estoqueAtual - item.getQtd());
        camisasRepository.save(camisa);
        item.setPedido(pedido);
        item.setCamisa(camisa);
        return itensPedidosRepository.save(item);
    }

    @Transactional
    public void excluir(Integer id) {
        ItensPedidosEntity item = itensPedidosRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item do pedido não encontrado!"));

        CamisasEntity camisa = item.getCamisa();
        camisa.setQuantidade((camisa.getQuantidade() == null ? 0 : camisa.getQuantidade()) + item.getQtd());
        camisasRepository.save(camisa);

        itensPedidosRepository.deleteById(id);
    }

    private void validarDtoAtualizacao(AtualizarItensPedidoDTO dto) {
        if (dto == null) {
            throw new RuntimeException("Dados da atualização não informados!");
        }
        if (dto.getIdPedido() == null) {
            throw new RuntimeException("Pedido não informado!");
        }
        if (dto.getItens() == null || dto.getItens().isEmpty()) {
            throw new RuntimeException("A atualização precisa ter pelo menos um item!");
        }
    }

    private Map<Integer, Integer> agruparQuantidadesAtuais(List<ItensPedidosEntity> itensAtuais) {
        Map<Integer, Integer> quantidades = new HashMap<>();
        for (ItensPedidosEntity item : itensAtuais) {
            quantidades.merge(item.getCamisa().getIdCamisa(), item.getQtd(), Integer::sum);
        }
        return quantidades;
    }

    private Map<Integer, Integer> agruparNovasQuantidades(List<AtualizarItensPedidoDTO.ItemPedidoDTO> itens) {
        Map<Integer, Integer> quantidades = new HashMap<>();
        for (AtualizarItensPedidoDTO.ItemPedidoDTO item : itens) {
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

    private Map<Integer, CamisasEntity> buscarEValidarEstoqueParaAtualizacao(
            Map<Integer, Integer> novasQuantidades,
            Map<Integer, Integer> quantidadesAtuais) {

        Map<Integer, CamisasEntity> camisas = new HashMap<>();

        for (Map.Entry<Integer, Integer> entrada : novasQuantidades.entrySet()) {
            CamisasEntity camisa = camisasRepository.findById(entrada.getKey())
                .orElseThrow(() -> new RuntimeException("Camisa não encontrada: id " + entrada.getKey()));

            Integer estoqueAtual = camisa.getQuantidade() == null ? 0 : camisa.getQuantidade();
            Integer quantidadeAnteriorNoPedido = quantidadesAtuais.getOrDefault(camisa.getIdCamisa(), 0);
            Integer estoqueDisponivelParaAtualizacao = estoqueAtual + quantidadeAnteriorNoPedido;
            Integer quantidadeNova = entrada.getValue();

            if (quantidadeNova > estoqueDisponivelParaAtualizacao) {
                throw new RuntimeException(
                    "Estoque insuficiente para atualizar a camisa " + camisa.getIdCamisa() +
                    ". Disponível considerando o pedido atual: " + estoqueDisponivelParaAtualizacao +
                    ", quantidade solicitada: " + quantidadeNova
                );
            }

            camisas.put(camisa.getIdCamisa(), camisa);
        }

        return camisas;
    }
}
