package com.inventory.inventorymanagementsystem.service;

import com.inventory.inventorymanagementsystem.dto.FornecedorCreateDTO;
import com.inventory.inventorymanagementsystem.dto.FornecedorDTO;
import com.inventory.inventorymanagementsystem.exception.DuplicateResourceException;
import com.inventory.inventorymanagementsystem.exception.ResourceNotFoundException;
import com.inventory.inventorymanagementsystem.model.Fornecedor;
import com.inventory.inventorymanagementsystem.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    @Transactional
    public FornecedorDTO criar(FornecedorCreateDTO dto) {
        if (dto.getCnpj() != null && !dto.getCnpj().isBlank()) {
            if (fornecedorRepository.existsByCnpj(dto.getCnpj())) {
                throw new DuplicateResourceException("Fornecedor", "CNPJ", dto.getCnpj());
            }
        }

        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome(dto.getNome());
        fornecedor.setCnpj(dto.getCnpj());
        fornecedor.setContato(dto.getContato());
        fornecedor.setTelefone(dto.getTelefone());
        fornecedor.setEmail(dto.getEmail());
        fornecedor.setEndereco(dto.getEndereco());
        fornecedor.setObservacao(dto.getObservacao());
        fornecedor.setAtivo(true);

        return toDTO(fornecedorRepository.save(fornecedor));
    }

    @Transactional(readOnly = true)
    public FornecedorDTO buscarPorId(Long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor", id));
        return toDTO(fornecedor);
    }

    @Transactional(readOnly = true)
    public FornecedorDTO buscarPorCnpj(String cnpj) {
        Fornecedor fornecedor = fornecedorRepository.findByCnpj(cnpj)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor com CNPJ " + cnpj + " nao encontrado"));
        return toDTO(fornecedor);
    }

    @Transactional(readOnly = true)
    public List<FornecedorDTO> listarTodos() {
        return fornecedorRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FornecedorDTO> listarAtivos() {
        return fornecedorRepository.findByAtivoTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FornecedorDTO> buscarPorNome(String nome) {
        return fornecedorRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FornecedorDTO atualizar(Long id, FornecedorCreateDTO dto) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor", id));

        if (dto.getCnpj() != null && !dto.getCnpj().isBlank()
                && !dto.getCnpj().equals(fornecedor.getCnpj())) {
            if (fornecedorRepository.existsByCnpj(dto.getCnpj())) {
                throw new DuplicateResourceException("Fornecedor", "CNPJ", dto.getCnpj());
            }
        }

        fornecedor.setNome(dto.getNome());
        fornecedor.setCnpj(dto.getCnpj());
        fornecedor.setContato(dto.getContato());
        fornecedor.setTelefone(dto.getTelefone());
        fornecedor.setEmail(dto.getEmail());
        fornecedor.setEndereco(dto.getEndereco());
        fornecedor.setObservacao(dto.getObservacao());

        return toDTO(fornecedorRepository.save(fornecedor));
    }

    @Transactional
    public void deletar(Long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor", id));
        fornecedor.setAtivo(false);
        fornecedorRepository.save(fornecedor);
    }

    private FornecedorDTO toDTO(Fornecedor fornecedor) {
        return FornecedorDTO.builder()
                .id(fornecedor.getId())
                .nome(fornecedor.getNome())
                .cnpj(fornecedor.getCnpj())
                .contato(fornecedor.getContato())
                .telefone(fornecedor.getTelefone())
                .email(fornecedor.getEmail())
                .endereco(fornecedor.getEndereco())
                .observacao(fornecedor.getObservacao())
                .ativo(fornecedor.getAtivo())
                .createdAt(fornecedor.getCreatedAt())
                .build();
    }
}