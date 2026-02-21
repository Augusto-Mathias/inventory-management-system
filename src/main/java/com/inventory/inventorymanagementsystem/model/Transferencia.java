package com.inventory.inventorymanagementsystem.model;

import com.inventory.inventorymanagementsystem.model.enums.StatusTransferencia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa uma transferência de produtos entre locais.
 *
 * Permite transferir múltiplos produtos em uma única operação.
 * Cada transferência gera automaticamente as movimentações de estoque correspondentes.
 *
 * Exemplo de uso:
 * - Transferir 105 TOAUCR + 5 PHICA da Loja para Fulfillment ML SC
 * - Transferir 50 produtos com defeito estético da Loja para Saldão
 */
@Entity
@Table(name = "transferencias")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Transferencia extends BaseEntity {

    /**
     * Local de origem dos produtos
     */
    @ManyToOne
    @JoinColumn(name = "local_origem_id", nullable = false)
    private LocalEstoque localOrigem;

    /**
     * Local de destino dos produtos
     */
    @ManyToOne
    @JoinColumn(name = "local_destino_id", nullable = false)
    private LocalEstoque localDestino;

    /**
     * Status da transferência
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusTransferencia status = StatusTransferencia.PENDENTE;

    /**
     * Observação sobre a transferência
     * Exemplo: "Reposição Full ML SC", "Envio Amazon", "Produtos com defeito estético"
     */
    @Column(length = 500)
    private String observacao;

    /**
     * Usuário responsável pela transferência
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * Itens da transferência (produtos e quantidades)
     */
    @OneToMany(mappedBy = "transferencia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemTransferencia> itens = new ArrayList<>();
}