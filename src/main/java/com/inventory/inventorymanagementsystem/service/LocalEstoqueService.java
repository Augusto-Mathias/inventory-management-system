package com.inventory.inventorymanagementsystem.service;

import com.inventory.inventorymanagementsystem.dto.LocalEstoqueCreateDTO;
import com.inventory.inventorymanagementsystem.dto.LocalEstoqueDTO;
import com.inventory.inventorymanagementsystem.exception.DuplicateResourceException;
import com.inventory.inventorymanagementsystem.exception.ResourceNotFoundException;
import com.inventory.inventorymanagementsystem.model.LocalEstoque;
import com.inventory.inventorymanagementsystem.model.enums.TipoLocal;
import com.inventory.inventorymanagementsystem.repository.LocalEstoqueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocalEstoqueService {

    private final LocalEstoqueRepository localEstoqueRepository;

    @Transactional
    public LocalEstoqueDTO criar(LocalEstoqueCreateDTO dto) {
        if (localEstoqueRepository.existsByNome(dto.getNome())) {
            throw new DuplicateResourceException("Local de Estoque", "nome", dto.getNome());
        }

        LocalEstoque local = new LocalEstoque();
        local.setNome(dto.getNome());
        local.setDescricao(dto.getDescricao());
        local.setTipo(dto.getTipo());
        local.setEndereco(dto.getEndereco());
        local.setAtivo(true);

        return toDTO(localEstoqueRepository.save(local));
    }

    @Transactional(readOnly = true)
    public LocalEstoqueDTO buscarPorId(Long id) {
        return toDTO(localEstoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local de Estoque", id)));
    }

    @Transactional(readOnly = true)
    public List<LocalEstoqueDTO> listarTodos() {
        return localEstoqueRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LocalEstoqueDTO> listarAtivos() {
        return localEstoqueRepository.findByAtivoTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LocalEstoqueDTO> listarPorTipo(TipoLocal tipo) {
        return localEstoqueRepository.findByTipo(tipo).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public LocalEstoqueDTO atualizar(Long id, LocalEstoqueCreateDTO dto) {
        LocalEstoque local = localEstoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local de Estoque", id));

        if (!local.getNome().equals(dto.getNome()) &&
                localEstoqueRepository.existsByNome(dto.getNome())) {
            throw new DuplicateResourceException("Local de Estoque", "nome", dto.getNome());
        }

        local.setNome(dto.getNome());
        local.setDescricao(dto.getDescricao());
        local.setTipo(dto.getTipo());
        local.setEndereco(dto.getEndereco());

        return toDTO(localEstoqueRepository.save(local));
    }

    @Transactional
    public void deletar(Long id) {
        LocalEstoque local = localEstoqueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Local de Estoque", id));
        local.setAtivo(false);
        localEstoqueRepository.save(local);
    }

    private LocalEstoqueDTO toDTO(LocalEstoque local) {
        return LocalEstoqueDTO.builder()
                .id(local.getId())
                .nome(local.getNome())
                .descricao(local.getDescricao())
                .tipo(local.getTipo())
                .endereco(local.getEndereco())
                .ativo(local.getAtivo())
                .createdAt(local.getCreatedAt())
                .build();
    }
}
