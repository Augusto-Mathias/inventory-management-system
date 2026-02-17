package com.inventory.inventorymanagementsystem.repository;

import com.inventory.inventorymanagementsystem.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNome(String nome);

    boolean existsByNome(String nome);

    List<Categoria> findByCategoriaPaiIsNull();

    List<Categoria> findByCategoriaPai(Categoria categoriaPai);
}