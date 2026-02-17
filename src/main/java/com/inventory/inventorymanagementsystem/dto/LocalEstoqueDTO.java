package com.inventory.inventorymanagementsystem.dto;

import com.inventory.inventorymanagementsystem.model.enums.TipoLocal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalEstoqueDTO {
    private Long id;
    private String nome;
    private String descricao;
    private TipoLocal tipo;
    private String endereco;
    private Boolean ativo;
    private LocalDateTime createdAt;
}
