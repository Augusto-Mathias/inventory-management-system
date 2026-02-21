package com.inventory.inventorymanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para retornar informações de um item de transferência.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemTransferenciaDTO {
    private Long id;
    private Long produtoId;
    private String produtoNome;
    private String produtoSku;
    private Integer quantidade;
}