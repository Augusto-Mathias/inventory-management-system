package com.inventory.inventorymanagementsystem.model;

import com.inventory.inventorymanagementsystem.model.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entidade que registra todas as movimentações de estoque do sistema.
 * Mantém histórico completo de entradas, saídas, transferências e ajustes.
 *
 * Cada movimentação armazena o estado anterior e atual do estoque para auditoria.
 */
@Entity
@Table(name = "movimentacoes_estoque")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class MovimentacaoEstoque extends BaseEntity {

    /**
     * Produto que foi movimentado
     */
    @ManyToOne
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    /**
     * Local onde ocorreu a movimentação
     */
    @ManyToOne
    @JoinColumn(name = "local_estoque_id", nullable = false)
    private LocalEstoque localEstoque;

    /**
     * Tipo de movimentação: ENTRADA, SAIDA, TRANSFERENCIA ou AJUSTE_INVENTARIO
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoMovimentacao tipo;

    /**
     * Quantidade movimentada.
     * Positivo para entradas, negativo para saídas.
     */
    @Column(nullable = false)
    private Integer quantidade;

    /**
     * Quantidade em estoque ANTES da movimentação (para auditoria)
     */
    @Column(nullable = false)
    private Integer quantidadeAnterior;

    /**
     * Quantidade em estoque DEPOIS da movimentação (para auditoria)
     */
    @Column(nullable = false)
    private Integer quantidadeAtual;

    /**
     * Observação sobre a movimentação.
     * Exemplo: "Compra NF 12345", "Venda pedido #789", "Perda por validade"
     */
    @Column(length = 500)
    private String observacao;

    /**
     * Usuário que realizou a movimentação (para auditoria)
     */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /**
     * Local de destino (usado apenas em transferências)
     */
    @ManyToOne
    @JoinColumn(name = "local_destino_id")
    private LocalEstoque localDestino;
}