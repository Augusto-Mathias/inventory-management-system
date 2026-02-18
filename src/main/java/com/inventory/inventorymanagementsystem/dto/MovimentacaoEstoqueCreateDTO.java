package com.inventory.inventorymanagementsystem.dto;

import com.inventory.inventorymanagementsystem.model.enums.MotivoMovimentacao;
import com.inventory.inventorymanagementsystem.model.enums.TipoMovimentacao;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para criar uma nova movimentação de estoque.
 * Valida os dados obrigatórios antes de persistir.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoEstoqueCreateDTO {

    @NotNull(message = "Produto e obrigatorio")
    private Long produtoId;

    @NotNull(message = "Local de estoque e obrigatorio")
    private Long localEstoqueId;

    @NotNull(message = "Tipo de movimentacao e obrigatorio")
    private TipoMovimentacao tipo;

    @NotNull(message = "Motivo da movimentacao e obrigatorio")
    private MotivoMovimentacao motivo;

    @NotNull(message = "Quantidade e obrigatoria")
    private Integer quantidade;

    private String observacao;

    @NotNull(message = "Usuario e obrigatorio")
    private Long usuarioId;

    /**
     * Local de destino - obrigatório apenas para transferências
     */
    private Long localDestinoId;
}