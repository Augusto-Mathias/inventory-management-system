package com.inventory.inventorymanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FornecedorDTO {
    private Long id;
    private String nome;
    private String cnpj;
    private String contato;
    private String telefone;
    private String email;
    private String endereco;
    private String observacao;
    private Boolean ativo;
    private LocalDateTime createdAt;
}