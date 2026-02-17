package com.inventory.inventorymanagementsystem.model;

import com.inventory.inventorymanagementsystem.model.enums.StatusProduto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "produtos")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Produto extends BaseEntity {

    // Identificacao basica
    @Column(nullable = false, length = 200)
    private String nome;

    @Column(nullable = false, unique = true, length = 50)
    private String sku;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    // Precificacao & Margem
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoCusto;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precoVenda;

    @Column(precision = 5, scale = 2)
    private BigDecimal margemPercentual;

    @Column(precision = 10, scale = 2)
    private BigDecimal precoPromocional;

    // Dropshipping
    @ManyToOne
    @JoinColumn(name = "fornecedor_id")
    private Fornecedor fornecedor;

    @Column(length = 500)
    private String urlFornecedor;

    private Integer prazoEntregaFornecedor;

    // Estoque & Status
    private Integer estoqueMinimo = 0;

    @Column(nullable = false)
    private Boolean ativo = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusProduto status = StatusProduto.RASCUNHO;

    // Dimensoes / Logistica
    @Column(precision = 10, scale = 3)
    private BigDecimal peso;

    @Column(precision = 10, scale = 2)
    private BigDecimal altura;

    @Column(precision = 10, scale = 2)
    private BigDecimal largura;

    @Column(precision = 10, scale = 2)
    private BigDecimal profundidade;

    // Visual & Caracteristicas
    @ElementCollection
    @CollectionTable(name = "produto_imagens", joinColumns = @JoinColumn(name = "produto_id"))
    @Column(name = "url", length = 500)
    private List<String> imagensUrls;

    @Column(length = 50)
    private String corPrincipal;

    @Column(length = 50)
    private String tamanhoPrincipal;

    // SEO & Marketing
    @Column(unique = true, length = 200)
    private String slug;

    @Column(length = 200)
    private String metaTitulo;

    @Column(length = 500)
    private String metaDescricao;

    // Campos fiscais Brasil
    @Column(length = 10)
    private String ncm;

    @Column(length = 14)
    private String gtin;

    @Column(length = 14)
    private String gtinTrib;

    @Column(length = 50)
    private String codigoBarrasAlternativo;

    @Column(length = 10)
    private String cest;

    // Flags
    @Column(nullable = false)
    private Boolean emDestaque = false;
}