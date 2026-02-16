package com.inventory.inventorymanagementsystem.service;

import com.inventory.inventorymanagementsystem.dto.UsuarioCreateDTO;
import com.inventory.inventorymanagementsystem.dto.UsuarioDTO;
import com.inventory.inventorymanagementsystem.dto.UsuarioUpdateDTO;
import com.inventory.inventorymanagementsystem.exception.DuplicateResourceException;
import com.inventory.inventorymanagementsystem.exception.ResourceNotFoundException;
import com.inventory.inventorymanagementsystem.model.Usuario;
import com.inventory.inventorymanagementsystem.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioDTO criar(UsuarioCreateDTO dto) {
        // Validar email único
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Usuario", "email", dto.getEmail());
        }

        // Criar entidade
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setSenha(dto.getSenha()); // TODO: Criptografar depois
        usuario.setPerfil(dto.getPerfil());
        usuario.setAtivo(true);

        // Salvar
        Usuario salvo = usuarioRepository.save(usuario);

        // Retornar DTO
        return toDTO(salvo);
    }

    @Transactional
    public UsuarioDTO atualizar(Long id, UsuarioUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        // Atualizar campos se fornecidos
        if (dto.getNome() != null) {
            usuario.setNome(dto.getNome());
        }
        if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(dto.getEmail())) {
                throw new DuplicateResourceException("Usuario", "email", dto.getEmail());
            }
            usuario.setEmail(dto.getEmail());
        }
        if (dto.getPerfil() != null) {
            usuario.setPerfil(dto.getPerfil());
        }
        if (dto.getAtivo() != null) {
            usuario.setAtivo(dto.getAtivo());
        }

        Usuario atualizado = usuarioRepository.save(usuario);
        return toDTO(atualizado);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
        return toDTO(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarAtivos() {
        return usuarioRepository.findByAtivoTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));

        // Soft delete - apenas desativa
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    // Método auxiliar para converter Entity -> DTO
    private UsuarioDTO toDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfil(usuario.getPerfil())
                .ativo(usuario.getAtivo())
                .createdAt(usuario.getCreatedAt())
                .build();
    }
}
