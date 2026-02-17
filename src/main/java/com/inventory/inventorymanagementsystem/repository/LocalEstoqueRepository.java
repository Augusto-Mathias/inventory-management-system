package com.inventory.inventorymanagementsystem.repository;

import com.inventory.inventorymanagementsystem.model.LocalEstoque;
import com.inventory.inventorymanagementsystem.model.enums.TipoLocal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalEstoqueRepository extends JpaRepository<LocalEstoque, Long> {

    List<LocalEstoque> findByTipo(TipoLocal tipo);

    List<LocalEstoque> findByAtivoTrue();

    boolean existsByNome(String nome);
}