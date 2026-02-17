package com.inventory.inventorymanagementsystem.repository;

import com.inventory.inventorymanagementsystem.model.EstoqueProduto;
import com.inventory.inventorymanagementsystem.model.LocalEstoque;
import com.inventory.inventorymanagementsystem.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstoqueProdutoRepository extends JpaRepository<EstoqueProduto, Long> {

    Optional<EstoqueProduto> findByProdutoAndLocalEstoque(Produto produto, LocalEstoque localEstoque);

    List<EstoqueProduto> findByProduto(Produto produto);

    List<EstoqueProduto> findByLocalEstoque(LocalEstoque localEstoque);

    @Query("SELECT SUM(e.quantidade) FROM EstoqueProduto e WHERE e.produto = :produto")
    Integer somarQuantidadePorProduto(@Param("produto") Produto produto);

    @Query("SELECT e FROM EstoqueProduto e WHERE e.quantidade <= e.produto.estoqueMinimo")
    List<EstoqueProduto> findEstoqueBaixo();
}