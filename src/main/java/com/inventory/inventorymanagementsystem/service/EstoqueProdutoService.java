package com.inventory.inventorymanagementsystem.service;

import com.inventory.inventorymanagementsystem.dto.EstoqueProdutoCreateDTO;
import com.inventory.inventorymanagementsystem.dto.EstoqueProdutoDTO;
import com.inventory.inventorymanagementsystem.dto.EstoqueProdutoUpdateDTO;
import com.inventory.inventorymanagementsystem.exception.DuplicateResourceException;
import com.inventory.inventorymanagementsystem.exception.ResourceNotFoundException;
import com.inventory.inventorymanagementsystem.model.EstoqueProduto;
import com.inventory.inventorymanagementsystem.model.LocalEstoque;
import com.inventory.inventorymanagementsystem.model.Produto;
import com.inventory.inventorymanagementsystem.repository.EstoqueProdutoRepository;
import com.inventory.inventorymanagementsystem.repository.LocalEstoqueRepository;
import com.inventory.inventorymanagementsystem.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstoqueProdutoService {

    private final EstoqueProdutoRepository estoqueProdutoRepository;
    private final ProdutoRepository produtoRepository;
    private final LocalEstoqueRepository localEstoqueRepository;

    @Transactional
    public EstoqueProdutoDTO criar(EstoqueProdutoCreateDTO dto) {
        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto", dto.getProdutoId()));

        LocalEstoque localEstoque = localEstoqueRepository.findById(dto.getLocalEstoqueId())
                .orElseThrow(() -> new ResourceNotFoundException("Local de Estoque", dto.getLocalEstoqueId()));

// Verifica se ja existe estoque para esse produto nesse local
        if (estoqueProdutoRepository.findByProdutoAndLocalEstoque(produto, localEstoque).isPresent()) {
            throw new DuplicateResourceException(
                    "Estoque",
                    "produto/local",
                    produto.getNome() + " em " + localEstoque.getNome());
        }

        EstoqueProduto estoque = new EstoqueProduto();
        estoque.setProduto(produto);
        estoque.setLocalEstoque(localEstoque);
        estoque.setQuantidade(dto.getQuantidade());

        return toDTO(estoqueProdutoRepository.save(estoque));
    }

    @Transactional(readOnly = true)
    public EstoqueProdutoDTO buscarPorId(Long id) {
        return toDTO(estoqueProdutoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque", id)));
    }

    @Transactional(readOnly = true)
    public List<EstoqueProdutoDTO> listarTodos() {
        return estoqueProdutoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EstoqueProdutoDTO> listarPorProduto(Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", produtoId));
        return estoqueProdutoRepository.findByProduto(produto).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EstoqueProdutoDTO> listarPorLocal(Long localEstoqueId) {
        LocalEstoque localEstoque = localEstoqueRepository.findById(localEstoqueId)
                .orElseThrow(() -> new ResourceNotFoundException("Local de Estoque", localEstoqueId));
        return estoqueProdutoRepository.findByLocalEstoque(localEstoque).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Integer obterQuantidadeTotal(Long produtoId) {
        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", produtoId));
        Integer total = estoqueProdutoRepository.somarQuantidadePorProduto(produto);
        return total != null ? total : 0;
    }

    @Transactional(readOnly = true)
    public List<EstoqueProdutoDTO> listarEstoqueBaixo() {
        return estoqueProdutoRepository.findEstoqueBaixo().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public EstoqueProdutoDTO atualizar(Long id, EstoqueProdutoUpdateDTO dto) {
        EstoqueProduto estoque = estoqueProdutoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estoque", id));

        estoque.setQuantidade(dto.getQuantidade());
        return toDTO(estoqueProdutoRepository.save(estoque));
    }

    @Transactional
    public void deletar(Long id) {
        if (!estoqueProdutoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Estoque", id);
        }
        estoqueProdutoRepository.deleteById(id);
    }

    private EstoqueProdutoDTO toDTO(EstoqueProduto estoque) {
        return EstoqueProdutoDTO.builder()
                .id(estoque.getId())
                .produtoId(estoque.getProduto().getId())
                .produtoNome(estoque.getProduto().getNome())
                .produtoSku(estoque.getProduto().getSku())
                .localEstoqueId(estoque.getLocalEstoque().getId())
                .localEstoqueNome(estoque.getLocalEstoque().getNome())
                .localEstoqueTipo(estoque.getLocalEstoque().getTipo().name())
                .quantidade(estoque.getQuantidade())
                .createdAt(estoque.getCreatedAt())
                .updatedAt(estoque.getUpdatedAt())
                .build();
    }
}