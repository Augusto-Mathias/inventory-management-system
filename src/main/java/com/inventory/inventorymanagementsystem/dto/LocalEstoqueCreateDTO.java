package com.inventory.inventorymanagementsystem.dto;

import com.inventory.inventorymanagementsystem.model.enums.TipoLocal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalEstoqueCreateDTO {

    @NotBlank(message = "Nome e obrigatorio")
    private String nome;

    private String descricao;

    @NotNull(message = "Tipo e obrigatorio")
    private TipoLocal tipo;

    private String endereco;
}