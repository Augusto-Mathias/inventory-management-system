package com.inventory.inventorymanagementsystem.repository;

import com.inventory.inventorymanagementsystem.model.LocalEstoque;
import com.inventory.inventorymanagementsystem.model.MovimentacaoEstoque;
import com.inventory.inventorymanagementsystem.model.Produto;
import com.inventory.inventorymanagementsystem.model.enums.MotivoMovimentacao;
import com.inventory.inventorymanagementsystem.model.enums.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para operações de banco de dados relacionadas a MovimentacaoEstoque.
 * Fornece queries personalizadas para relatórios e consultas específicas.
 */
@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    /**
     * Lista todas as movimentações de um produto específico
     */
    List<MovimentacaoEstoque> findByProduto(Produto produto);

    /**
     * Lista todas as movimentações de um local específico
     */
    List<MovimentacaoEstoque> findByLocalEstoque(LocalEstoque localEstoque);

    /**
     * Lista movimentações por tipo (ENTRADA, SAIDA, etc)
     */
    List<MovimentacaoEstoque> findByTipo(TipoMovimentacao tipo);

    /**
     * Lista movimentações de um produto em um local específico
     */
    List<MovimentacaoEstoque> findByProdutoAndLocalEstoque(Produto produto, LocalEstoque localEstoque);

    /**
     * Lista movimentações dentro de um período de datas
     */
    @Query("SELECT m FROM MovimentacaoEstoque m WHERE m.createdAt BETWEEN :dataInicio AND :dataFim ORDER BY m.createdAt DESC")
    List<MovimentacaoEstoque> findByPeriodo(
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim
    );

    /**
     * Lista últimas movimentações (para dashboard)
     */
    @Query("SELECT m FROM MovimentacaoEstoque m ORDER BY m.createdAt DESC")
    List<MovimentacaoEstoque> findUltimasMovimentacoes();

    /**
     * Soma total de entradas de um produto
     */
    @Query("SELECT SUM(m.quantidade) FROM MovimentacaoEstoque m WHERE m.produto = :produto AND m.tipo = 'ENTRADA'")
    Integer somarEntradas(@Param("produto") Produto produto);

    /**
     * Soma total de saídas de um produto (valor absoluto)
     */
    @Query("SELECT SUM(ABS(m.quantidade)) FROM MovimentacaoEstoque m WHERE m.produto = :produto AND m.tipo = 'SAIDA'")
    Integer somarSaidas(@Param("produto") Produto produto);

    /**
     * Lista movimentações por motivo específico
     */
    List<MovimentacaoEstoque> findByMotivo(MotivoMovimentacao motivo);
}