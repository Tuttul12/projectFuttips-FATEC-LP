package com.futtips.project.entities;

import java.io.Serializable;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name="pessoas")
@Inheritance(strategy = InheritanceType.JOINED)
public class PessoasEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(max = 50, message = "O nome deve ter no máximo 50 caracteres.")
    @Column(length = 50, nullable = false)
    private String nome;

    @NotBlank(message = "O CPF é obrigatório.")
    @CPF(message = "CPF inválido.")
    @Size(max = 14, message = "O CPF deve ter no máximo 14 caracteres.")
    @Column(length = 14, nullable = false, unique = true)
    private String cpf;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "E-mail inválido.")
    @Size(max = 254, message = "O e-mail deve ter no máximo 254 caracteres.")
    @Column(length = 254, nullable = false, unique = true)
    private String email;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, max = 255, message = "A senha deve ter entre 6 e 255 caracteres.")
    @Column(length = 255, nullable = false)
    private String senha;

    @NotNull(message = "O status ativo deve ser informado.")
    @Column(nullable = false)
    private Boolean ativo = true;
}
