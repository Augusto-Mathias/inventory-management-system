package com.inventory.inventorymanagementsystem.dto;

import com.inventory.inventorymanagementsystem.model.enums.StatusProduto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoCreateDTO {

    // Identificacao
    @NotBlank(message = "Nome e obrigatorio")
    private String nome;

    @NotBlank(message = "SKU e obrigatorio")
    private String sku;

    private String descricao;
    private Long categoriaId;

    // Precificacao
    @NotNull(message = "Preco de custo e obrigatorio")
    @Positive(message = "Preco de custo deve ser positivo")
    private BigDecimal precoCusto;

    @NotNull(message = "Preco de venda e obrigatorio")
    @Positive(message = "Preco de venda deve ser positivo")
    private BigDecimal precoVenda;

    private BigDecimal precoPromocional;

    // Dropshipping
    private Long fornecedorId;
    private String urlFornecedor;
    private Integer prazoEntregaFornecedor;

    // Estoque
    private Integer estoqueMinimo = 0;
    private StatusProduto status = StatusProduto.RASCUNHO;

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
    private Boolean emDestaque = false;
}