package com.inventory.inventorymanagementsystem.repository;

import com.inventory.inventorymanagementsystem.model.Produto;
import com.inventory.inventorymanagementsystem.model.enums.StatusProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findBySku(String sku);

    boolean existsBySku(String sku);

    Optional<Produto> findBySlug(String slug);

    List<Produto> findByStatus(StatusProduto status);

    List<Produto> findByAtivoTrue();

    List<Produto> findByEmDestaqueTrue();

    List<Produto> findByNomeContainingIgnoreCase(String nome);

    List<Produto> findByCategoriaId(Long categoriaId);

    List<Produto> findByFornecedorId(Long fornecedorId);

    // TODO: Habilitar depois de criar EstoqueProduto
    // @Query("SELECT p FROM Produto p JOIN EstoqueProduto e ON e.produto = p WHERE e.quantidade <= p.estoqueMinimo")
    // List<Produto> findProdutosComEstoqueBaixo();
}