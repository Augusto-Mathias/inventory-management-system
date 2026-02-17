package com.inventory.inventorymanagementsystem.service;

import com.inventory.inventorymanagementsystem.dto.ProdutoCreateDTO;
import com.inventory.inventorymanagementsystem.dto.ProdutoDTO;
import com.inventory.inventorymanagementsystem.exception.DuplicateResourceException;
import com.inventory.inventorymanagementsystem.exception.ResourceNotFoundException;
import com.inventory.inventorymanagementsystem.model.Categoria;
import com.inventory.inventorymanagementsystem.model.Fornecedor;
import com.inventory.inventorymanagementsystem.model.Produto;
import com.inventory.inventorymanagementsystem.model.enums.StatusProduto;
import com.inventory.inventorymanagementsystem.repository.CategoriaRepository;
import com.inventory.inventorymanagementsystem.repository.FornecedorRepository;
import com.inventory.inventorymanagementsystem.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final FornecedorRepository fornecedorRepository;

    @Transactional
    public ProdutoDTO criar(ProdutoCreateDTO dto) {
        // Validar SKU unico
        if (produtoRepository.existsBySku(dto.getSku())) {
            throw new DuplicateResourceException("Produto", "SKU", dto.getSku());
        }

        Produto produto = new Produto();
        preencherProduto(produto, dto);

        return toDTO(produtoRepository.save(produto));
    }

    @Transactional(readOnly = true)
    public ProdutoDTO buscarPorId(Long id) {
        return toDTO(produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", id)));
    }

    @Transactional(readOnly = true)
    public ProdutoDTO buscarPorSku(String sku) {
        return toDTO(produtoRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com SKU " + sku + " nao encontrado")));
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarAtivos() {
        return produtoRepository.findByAtivoTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarDestaques() {
        return produtoRepository.findByEmDestaqueTrue().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarPorStatus(StatusProduto status) {
        return produtoRepository.findByStatus(status).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> buscarPorNome(String nome) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarPorCategoria(Long categoriaId) {
        return produtoRepository.findByCategoriaId(categoriaId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProdutoDTO> listarPorFornecedor(Long fornecedorId) {
        return produtoRepository.findByFornecedorId(fornecedorId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProdutoDTO atualizar(Long id, ProdutoCreateDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", id));

        // Validar SKU se foi alterado
        if (!produto.getSku().equals(dto.getSku()) && produtoRepository.existsBySku(dto.getSku())) {
            throw new DuplicateResourceException("Produto", "SKU", dto.getSku());
        }

        preencherProduto(produto, dto);
        return toDTO(produtoRepository.save(produto));
    }

    @Transactional
    public void deletar(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", id));
        produto.setAtivo(false);
        produto.setStatus(StatusProduto.INATIVO);
        produtoRepository.save(produto);
    }

    // Metodo auxiliar para preencher produto com dados do DTO
    private void preencherProduto(Produto produto, ProdutoCreateDTO dto) {
        produto.setNome(dto.getNome());
        produto.setSku(dto.getSku());
        produto.setDescricao(dto.getDescricao());
        produto.setPrecoCusto(dto.getPrecoCusto());
        produto.setPrecoVenda(dto.getPrecoVenda());
        produto.setPrecoPromocional(dto.getPrecoPromocional());
        produto.setUrlFornecedor(dto.getUrlFornecedor());
        produto.setPrazoEntregaFornecedor(dto.getPrazoEntregaFornecedor());
        produto.setEstoqueMinimo(dto.getEstoqueMinimo());
        produto.setStatus(dto.getStatus() != null ? dto.getStatus() : StatusProduto.RASCUNHO);
        produto.setPeso(dto.getPeso());
        produto.setAltura(dto.getAltura());
        produto.setLargura(dto.getLargura());
        produto.setProfundidade(dto.getProfundidade());
        produto.setImagensUrls(dto.getImagensUrls());
        produto.setCorPrincipal(dto.getCorPrincipal());
        produto.setTamanhoPrincipal(dto.getTamanhoPrincipal());
        produto.setMetaTitulo(dto.getMetaTitulo());
        produto.setMetaDescricao(dto.getMetaDescricao());
        produto.setNcm(dto.getNcm());
        produto.setGtin(dto.getGtin());
        produto.setGtinTrib(dto.getGtinTrib());
        produto.setCodigoBarrasAlternativo(dto.getCodigoBarrasAlternativo());
        produto.setCest(dto.getCest());
        produto.setEmDestaque(dto.getEmDestaque() != null ? dto.getEmDestaque() : false);

        // Calcular margem automaticamente
        if (dto.getPrecoCusto() != null && dto.getPrecoVenda() != null
                && dto.getPrecoVenda().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal margem = dto.getPrecoVenda()
                    .subtract(dto.getPrecoCusto())
                    .divide(dto.getPrecoVenda(), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100"))
                    .setScale(2, RoundingMode.HALF_UP);
            produto.setMargemPercentual(margem);
        }

        // Gerar slug automaticamente se nao informado
        if (dto.getSlug() == null || dto.getSlug().isBlank()) {
            produto.setSlug(gerarSlug(dto.getNome()));
        } else {
            produto.setSlug(dto.getSlug());
        }

        // Categoria
        if (dto.getCategoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoria", dto.getCategoriaId()));
            produto.setCategoria(categoria);
        }

        // Fornecedor
        if (dto.getFornecedorId() != null) {
            Fornecedor fornecedor = fornecedorRepository.findById(dto.getFornecedorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Fornecedor", dto.getFornecedorId()));
            produto.setFornecedor(fornecedor);
        }
    }

    // Gerar slug a partir do nome
    private String gerarSlug(String nome) {
        return Normalizer.normalize(nome, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");
    }

    // Converter Entity para DTO
    private ProdutoDTO toDTO(Produto produto) {
        return ProdutoDTO.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .sku(produto.getSku())
                .descricao(produto.getDescricao())
                .categoriaId(produto.getCategoria() != null ? produto.getCategoria().getId() : null)
                .categoriaNome(produto.getCategoria() != null ? produto.getCategoria().getNome() : null)
                .precoCusto(produto.getPrecoCusto())
                .precoVenda(produto.getPrecoVenda())
                .margemPercentual(produto.getMargemPercentual())
                .precoPromocional(produto.getPrecoPromocional())
                .fornecedorId(produto.getFornecedor() != null ? produto.getFornecedor().getId() : null)
                .fornecedorNome(produto.getFornecedor() != null ? produto.getFornecedor().getNome() : null)
                .urlFornecedor(produto.getUrlFornecedor())
                .prazoEntregaFornecedor(produto.getPrazoEntregaFornecedor())
                .estoqueMinimo(produto.getEstoqueMinimo())
                .ativo(produto.getAtivo())
                .status(produto.getStatus())
                .peso(produto.getPeso())
                .altura(produto.getAltura())
                .largura(produto.getLargura())
                .profundidade(produto.getProfundidade())
                .imagensUrls(produto.getImagensUrls())
                .corPrincipal(produto.getCorPrincipal())
                .tamanhoPrincipal(produto.getTamanhoPrincipal())
                .slug(produto.getSlug())
                .metaTitulo(produto.getMetaTitulo())
                .metaDescricao(produto.getMetaDescricao())
                .ncm(produto.getNcm())
                .gtin(produto.getGtin())
                .gtinTrib(produto.getGtinTrib())
                .codigoBarrasAlternativo(produto.getCodigoBarrasAlternativo())
                .cest(produto.getCest())
                .emDestaque(produto.getEmDestaque())
                .createdAt(produto.getCreatedAt())
                .updatedAt(produto.getUpdatedAt())
                .build();
    }
}