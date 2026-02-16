package com.inventory.inventorymanagementsystem.dto;

import com.inventory.inventorymanagementsystem.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nome;
    private String email;
    private Role perfil;
    private Boolean ativo;
    private LocalDateTime createdAt;
}