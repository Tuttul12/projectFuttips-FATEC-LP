package com.futtips.project.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.futtips.project.entities.FuncionariosEntity;
import com.futtips.project.entities.dto.ClienteParaFuncionarioDTO;
import com.futtips.project.entities.dto.CriarFuncionarioDTO;
import com.futtips.project.repositories.FuncionariosRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class FuncionariosService {

    @Autowired
    private FuncionariosRepository funcionariosRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<FuncionariosEntity> buscarTodos() {
        return funcionariosRepository.findAll();
    }

    public Optional<FuncionariosEntity> buscarFuncionario(Integer id) {
        return funcionariosRepository.findById(id);
    }

    @Transactional
    public FuncionariosEntity criar(CriarFuncionarioDTO dto) {

        // Chama a procedure
        entityManager.createNativeQuery(
            "EXEC sp_criar_pessoa_funcionario " +
            "@nome = :nome, " +
            "@cpf = :cpf, " +
            "@email = :email, " +
            "@senha = :senha, " +
            "@salario = :salario, " +
            "@codigo_cargo = :codigoCargo")
            .setParameter("nome",        dto.getNome())
            .setParameter("cpf",         dto.getCpf())
            .setParameter("email",       dto.getEmail())
            .setParameter("senha",       dto.getSenha())
            .setParameter("salario",     dto.getSalario())
            .setParameter("codigoCargo", dto.getCodigoCargo())
            .executeUpdate();

        // Busca o funcionario recém criado pelo CPF
        return funcionariosRepository.findByCpf(dto.getCpf())
            .orElseThrow(() -> new RuntimeException("Erro ao buscar funcionário após cadastro"));
    }

    public FuncionariosEntity editar(int id, FuncionariosEntity funcionariosEntity) {
        Optional<FuncionariosEntity> funcionarios = funcionariosRepository.findById(id);
        if (funcionarios.isPresent()) {
            FuncionariosEntity funcionariosParaAtualizar = funcionarios.get();
            funcionariosParaAtualizar.setSalario(funcionariosEntity.getSalario());
            funcionariosParaAtualizar.setCargo(funcionariosEntity.getCargo());
            funcionariosParaAtualizar.setNome(funcionariosEntity.getNome());
            funcionariosParaAtualizar.setCpf(funcionariosEntity.getCpf());
            funcionariosParaAtualizar.setEmail(funcionariosEntity.getEmail());
            if (funcionariosEntity.getSenha() != null && !funcionariosEntity.getSenha().isBlank()) {
                funcionariosParaAtualizar.setSenha(funcionariosEntity.getSenha());
            }
            return funcionariosRepository.save(funcionariosParaAtualizar);
        } else {
            return null;
        }      
    }

    public FuncionariosEntity exluir(Integer id) {
       FuncionariosEntity funcionarios = funcionariosRepository.findById(id).orElseThrow(() -> new RuntimeException("Funcionario não encontrado!"));
       funcionariosRepository.deleteById(id);
       return funcionarios;
    }

    @Transactional
    public FuncionariosEntity clienteParaFuncionario(ClienteParaFuncionarioDTO dto) {

        entityManager.createNativeQuery(
            "EXEC sp_cliente_para_funcionario " +
            "@id_pessoa = :idPessoa, " +
            "@salario = :salario, " +
            "@codigo_cargo = :codigoCargo")
            .setParameter("idPessoa",    dto.getIdPessoa())
            .setParameter("salario",     dto.getSalario())
            .setParameter("codigoCargo", dto.getCodigoCargo())
            .executeUpdate();

        return funcionariosRepository.findById(dto.getIdPessoa())
            .orElseThrow(() -> new RuntimeException("Erro ao buscar funcionário após conversão"));
    }

}
