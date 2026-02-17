package com.inventory.inventorymanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorCreateDTO {

    @NotBlank(message = "Nome é obrigatorio")
    private String nome;

    private String cnpj;
    private String contato;
    private String telefone;
    private String email;
    private String endereco;
    private String observacao;
}