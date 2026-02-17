package com.inventory.inventorymanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estoque_produtos",
        uniqueConstraints = @UniqueConstraint(columnNames = {"produto_id", "local_estoque_id"}))
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueProduto extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "local_estoque_id", nullable = false)
    private LocalEstoque localEstoque;

    @Column(nullable = false)
    private Integer quantidade = 0;
}