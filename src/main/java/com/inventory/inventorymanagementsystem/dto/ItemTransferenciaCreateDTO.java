package com.inventory.inventorymanagementsystem.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para criar um item de transferência.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemTransferenciaCreateDTO {

    @NotNull(message = "Produto e obrigatorio")
    private Long produtoId;

    @NotNull(message = "Quantidade e obrigatoria")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private Integer quantidade;
}