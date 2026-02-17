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
public class EstoqueProdutoDTO {
    private Long id;
    private Long produtoId;
    private String produtoNome;
    private String produtoSku;
    private Long localEstoqueId;
    private String localEstoqueNome;
    private String localEstoqueTipo;
    private Integer quantidade;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}