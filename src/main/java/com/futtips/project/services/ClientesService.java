package com.futtips.project.services;


import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.futtips.project.entities.ClientesEntity;
import com.futtips.project.entities.dto.CriarClienteDTO;
import com.futtips.project.entities.dto.EditarClienteDTO;
import com.futtips.project.entities.dto.FuncionarioParaClienteDTO;
import com.futtips.project.repositories.ClientesRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class ClientesService {

    @Autowired
    private ClientesRepository clientesRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<ClientesEntity> buscarTodos() {
        return clientesRepository.findAll();
    }

    public Optional<ClientesEntity> buscarClientes(Integer id) {
        return clientesRepository.findById(id);
    }

    @Transactional
    public ClientesEntity criar(CriarClienteDTO dto) {

        entityManager.createNativeQuery(
            "EXEC sp_criar_pessoa_cliente " +
            ":nome, :cpf, :email, :senha, " +
            ":nascimento, :telefone, " +
            ":rua, :numero, :bairro, :cidade, :estado, :cep")
            .setParameter("nome",       dto.getNome())
            .setParameter("cpf",        dto.getCpf())
            .setParameter("email",      dto.getEmail())
            .setParameter("senha",      dto.getSenha())
            .setParameter("nascimento", dto.getNascimento())
            .setParameter("telefone",   dto.getTelefone())
            .setParameter("rua",        dto.getRua())
            .setParameter("numero",     dto.getNumero())
            .setParameter("bairro",     dto.getBairro())
            .setParameter("cidade",     dto.getCidade())
            .setParameter("estado",     dto.getEstado())
            .setParameter("cep",        dto.getCep())
            .executeUpdate();

        // Busca o cliente pelo CPF após inserção
        return clientesRepository.findByCpf(dto.getCpf())
            .orElseThrow(() -> new RuntimeException("Erro ao buscar cliente após inserção"));
    }

    @Transactional
    public ClientesEntity editar(int id, EditarClienteDTO dto) {

        if (!clientesRepository.existsById(id)) {
            return null;
        }

        entityManager.createNativeQuery(
            "EXEC sp_editar_cliente " +
            "@id_cliente = :idCliente, " +
            "@nome = :nome, " +
            "@cpf = :cpf, " +
            "@email = :email, " +
            "@senha = :senha, " +
            "@nascimento = :nascimento, " +
            "@telefone = :telefone, " +
            "@rua = :rua, " +
            "@numero = :numero, " +
            "@bairro = :bairro, " +
            "@cidade = :cidade, " +
            "@estado = :estado, " +
            "@cep = :cep"
        )
        .setParameter("idCliente", id)
        .setParameter("nome", dto.getNome())
        .setParameter("cpf", dto.getCpf())
        .setParameter("email", dto.getEmail())
        .setParameter("senha", dto.getSenha())
        .setParameter("nascimento", dto.getNascimento())
        .setParameter("telefone", dto.getTelefone())
        .setParameter("rua", dto.getRua())
        .setParameter("numero", dto.getNumero())
        .setParameter("bairro", dto.getBairro())
        .setParameter("cidade", dto.getCidade())
        .setParameter("estado", dto.getEstado())
        .setParameter("cep", dto.getCep())
        .executeUpdate();

        return clientesRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Erro ao buscar cliente após edição"));
    }

    @Transactional
    public ClientesEntity exluir(Integer id) {
        ClientesEntity clientes = clientesRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));
        clientesRepository.deleteById(id);
        return clientes;
    }

    @Transactional
    public ClientesEntity funcionarioParaCliente(FuncionarioParaClienteDTO dto) {

        entityManager.createNativeQuery(
            "EXEC sp_funcionario_para_cliente " +
            "@id_pessoa = :idPessoa, " +
            "@nascimento = :nascimento, " +
            "@telefone = :telefone")
            .setParameter("idPessoa",   dto.getIdPessoa())
            .setParameter("nascimento", dto.getNascimento())
            .setParameter("telefone",   dto.getTelefone())
            .executeUpdate();

        return clientesRepository.findById(dto.getIdPessoa())
            .orElseThrow(() -> new RuntimeException("Erro ao buscar cliente após conversão"));
    }
}