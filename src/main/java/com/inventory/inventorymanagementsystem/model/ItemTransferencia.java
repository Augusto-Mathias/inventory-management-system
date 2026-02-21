package com.inventory.inventorymanagementsystem.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Representa um item (produto + quantidade) dentro de uma transferência.
 *
 * Cada transferência pode ter vários itens.
 *
 * Exemplo:
 * Transferencia #5: Loja → Full ML SC
 *   ├─ Item 1: 105 unidades de cadernos
 *   ├─ Item 2: 5 unidades de carregadores
 *  *   └─ Item 3: 10 unidades de NSI
 */
@Entity
@Table(name = "itens_transferencia")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ItemTransferencia extends BaseEntity {

    /**
     * Transferência a qual este item pertence
     */
    @ManyToOne
    @JoinColumn(name = "transferencia_id", nullable = false)
    private Transferencia transferencia;

    /**
     * Produto sendo transferido
     */
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    /**
     * Quantidade do produto sendo transferida
     */
    @Column(nullable = false)
    private Integer quantidade;
}