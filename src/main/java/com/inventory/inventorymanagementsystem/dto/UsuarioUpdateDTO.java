package com.inventory.inventorymanagementsystem.dto;

import com.inventory.inventorymanagementsystem.model.enums.Role;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioUpdateDTO {
    private String nome;

    @Email(message = "Email inválido")
    private String email;

    private Role perfil;
    private Boolean ativo;
}