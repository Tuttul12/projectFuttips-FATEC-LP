package com.futtips.project.entities;

import java.io.Serializable;

import com.futtips.project.entities.pk.EnderecoPK;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="enderecos")
public class EnderecosEntity implements Serializable {

    @Valid
    @NotNull(message = "A chave do endereço é obrigatória.")
    @EmbeddedId
    private EnderecoPK id = new EnderecoPK();

    @NotBlank(message = "A rua é obrigatória.")
    @Size(max = 100, message = "A rua deve ter no máximo 100 caracteres.")
    @Column(length = 100, nullable = false)
    private String rua;

    @Min(value = 1, message = "O número do endereço deve ser maior que zero.")
    @Column(nullable = false)
    private int numero;

    @NotBlank(message = "O bairro é obrigatório.")
    @Size(max = 100, message = "O bairro deve ter no máximo 100 caracteres.")
    @Column(length = 100, nullable = false)
    private String bairro;

    @NotBlank(message = "A cidade é obrigatória.")
    @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres.")
    @Column(length = 100, nullable = false)
    private String cidade;

    @NotBlank(message = "O estado é obrigatório.")
    @Size(max = 100, message = "O estado deve ter no máximo 100 caracteres.")
    @Column(length = 100, nullable = false)
    private String estado;

    @NotBlank(message = "O CEP é obrigatório.")
    @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "CEP inválido. Use o formato 00000-000 ou 00000000.")
    @Size(max = 12, message = "O CEP deve ter no máximo 12 caracteres.")
    @Column(length = 12, nullable = false)
    private String cep;
}
