package com.inventory.inventorymanagementsystem.dto;

import com.inventory.inventorymanagementsystem.model.enums.TipoMovimentacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para retornar informações de movimentação de estoque.
 * Inclui dados denormalizados para facilitar exibição.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoEstoqueDTO {
    private Long id;
    private Long produtoId;
    private String produtoNome;
    private String produtoSku;
    private Long localEstoqueId;
    private String localEstoqueNome;
    private TipoMovimentacao tipo;
    private Integer quantidade;
    private Integer quantidadeAnterior;
    private Integer quantidadeAtual;
    private String observacao;
    private Long usuarioId;
    private String usuarioNome;
    private Long localDestinoId;
    private String localDestinoNome;
    private LocalDateTime createdAt;
}