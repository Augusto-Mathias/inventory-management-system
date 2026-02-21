package com.inventory.inventorymanagementsystem.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO para criar uma nova transferência.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaCreateDTO {

    @NotNull(message = "Local de origem e obrigatorio")
    private Long localOrigemId;

    @NotNull(message = "Local de destino e obrigatorio")
    private Long localDestinoId;

    @NotEmpty(message = "A transferencia deve ter pelo menos um item")
    @Valid
    private List<ItemTransferenciaCreateDTO> itens;

    private String observacao;

    @NotNull(message = "Usuario e obrigatorio")
    private Long usuarioId;
}