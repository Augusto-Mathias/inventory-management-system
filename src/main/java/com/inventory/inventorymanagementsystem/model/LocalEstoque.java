package com.inventory.inventorymanagementsystem.model;

import com.inventory.inventorymanagementsystem.model.enums.TipoLocal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entidade que representa um local de armazenamento de estoque.
 * Pode ser uma loja física, fulfillment, área de reparo, saldão, etc.
 */
@Entity
@Table(name = "locais_estoque")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class LocalEstoque extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoLocal tipo;

    @Column(length = 255)
    private String endereco;

    @Column(nullable = false)
    private Boolean ativo = true;

    /**
     * Indica se produtos neste local estão disponíveis para venda.
     *
     * true = Conta no estoque vendível (Loja, Fulfillment, Saldão)
     * false = Não conta no estoque vendível (Reparo, Devolução, Defeito)
     *
     * Usado para calcular "Estoque Físico Projeção de Vendas"
     */
    @Column(nullable = false)
    private Boolean vendivel = true;
}