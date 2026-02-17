package com.inventory.inventorymanagementsystem.service;

import com.inventory.inventorymanagementsystem.dto.CategoriaCreateDTO;
import com.inventory.inventorymanagementsystem.dto.CategoriaDTO;
import com.inventory.inventorymanagementsystem.exception.DuplicateResourceException;
import com.inventory.inventorymanagementsystem.exception.ResourceNotFoundException;
import com.inventory.inventorymanagementsystem.model.Categoria;
import com.inventory.inventorymanagementsystem.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional
    public CategoriaDTO criar(CategoriaCreateDTO dto) {
        if (categoriaRepository.existsByNome(dto.getNome())) {
            throw new DuplicateResourceException("Categoria", "nome", dto.getNome());
        }

        Categoria categoria = new Categoria();
        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());

        if (dto.getCategoriaPaiId() != null) {
            Categoria pai = categoriaRepository.findById(dto.getCategoriaPaiId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria pai", dto.getCategoriaPaiId()));
            categoria.setCategoriaPai(pai);
        }

        return toDTO(categoriaRepository.save(categoria));
    }

    @Transactional(readOnly = true)
    public CategoriaDTO buscarPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));
        return toDTO(categoria);
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarRaiz() {
        return categoriaRepository.findByCategoriaPaiIsNull().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listarSubcategorias(Long categoriaPaiId) {
        Categoria pai = categoriaRepository.findById(categoriaPaiId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", categoriaPaiId));
        return categoriaRepository.findByCategoriaPai(pai).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoriaDTO atualizar(Long id, CategoriaCreateDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria", id));

        if (!categoria.getNome().equals(dto.getNome()) && categoriaRepository.existsByNome(dto.getNome())) {
            throw new DuplicateResourceException("Categoria", "nome", dto.getNome());
        }

        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());

        if (dto.getCategoriaPaiId() != null) {
            Categoria pai = categoriaRepository.findById(dto.getCategoriaPaiId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria pai", dto.getCategoriaPaiId()));
            categoria.setCategoriaPai(pai);
        } else {
            categoria.setCategoriaPai(null);
        }

        return toDTO(categoriaRepository.save(categoria));
    }

    @Transactional
    public void deletar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria", id);
        }
        categoriaRepository.deleteById(id);
    }

    private CategoriaDTO toDTO(Categoria categoria) {
        return CategoriaDTO.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .descricao(categoria.getDescricao())
                .categoriaPaiId(categoria.getCategoriaPai() != null ? categoria.getCategoriaPai().getId() : null)
                .categoriaPaiNome(categoria.getCategoriaPai() != null ? categoria.getCategoriaPai().getNome() : null)
                .createdAt(categoria.getCreatedAt())
                .build();
    }
}