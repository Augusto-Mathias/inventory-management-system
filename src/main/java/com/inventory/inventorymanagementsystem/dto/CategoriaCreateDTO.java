package com.inventory.inventorymanagementsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaCreateDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    private String descricao;

    private Long categoriaPaiId;
}