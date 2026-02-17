package com.inventory.inventorymanagementsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueProdutoCreateDTO {

    @NotNull(message = "Produto e obrigatorio")
    private Long produtoId;

    @NotNull(message = "Local de estoque e obrigatorio")
    private Long localEstoqueId;

    @NotNull(message = "Quantidade e obrigatoria")
    @Min(value = 0, message = "Quantidade deve ser maior ou igual a zero")
    private Integer quantidade;
}