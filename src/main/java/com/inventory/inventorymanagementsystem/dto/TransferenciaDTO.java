package com.inventory.inventorymanagementsystem.dto;

import com.inventory.inventorymanagementsystem.model.enums.StatusTransferencia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para retornar informações de uma transferência.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaDTO {
    private Long id;
    private Long localOrigemId;
    private String localOrigemNome;
    private Long localDestinoId;
    private String localDestinoNome;
    private StatusTransferencia status;
    private String observacao;
    private Long usuarioId;
    private String usuarioNome;
    private List<ItemTransferenciaDTO> itens;
    private LocalDateTime createdAt;
}