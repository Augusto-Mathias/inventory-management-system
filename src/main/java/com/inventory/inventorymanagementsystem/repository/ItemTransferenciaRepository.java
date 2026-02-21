package com.inventory.inventorymanagementsystem.repository;

import com.inventory.inventorymanagementsystem.model.ItemTransferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository para operações de banco de dados relacionadas a ItemTransferencia.
 */
@Repository
public interface ItemTransferenciaRepository extends JpaRepository<ItemTransferencia, Long> {
}