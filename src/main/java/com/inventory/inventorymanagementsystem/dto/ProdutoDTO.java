package com.inventory.inventorymanagementsystem.dto;

import com.inventory.inventorymanagementsystem.model.enums.StatusProduto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {
    // Identificacao
    private Long id;
    private String nome;
    private String sku;
    private String descricao;
    private Long categoriaId;
    private String categoriaNome;

    // Precificacao
    private BigDecimal precoCusto;
    private BigDecimal precoVenda;
    private BigDecimal margemPercentual;
    private BigDecimal precoPromocional;

    // Dropshipping
    private Long fornecedorId;
    private String fornecedorNome;
    private String urlFornecedor;
    private Integer prazoEntregaFornecedor;

    // Estoque & Status
    private Integer estoqueMinimo;
    private Integer quantidadeTotal;
    private Boolean ativo;
    private StatusProduto status;

    // Dimensoes
    private BigDecimal peso;
    private BigDecimal altura;
    private BigDecimal largura;
    private BigDecimal profundidade;

    // Visual
    private List<String> imagensUrls;
    private String corPrincipal;
    private String tamanhoPrincipal;

    // SEO
    private String slug;
    private String metaTitulo;
    private String metaDescricao;

    // Fiscal
    private String ncm;
    private String gtin;
    private String gtinTrib;
    private String codigoBarrasAlternativo;
    private String cest;

    // Flags
    private Boolean emDestaque;

    // Auditoria
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}