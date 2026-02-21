package com.inventory.inventorymanagementsystem.repository;

import com.inventory.inventorymanagementsystem.model.LocalEstoque;
import com.inventory.inventorymanagementsystem.model.Transferencia;
import com.inventory.inventorymanagementsystem.model.enums.StatusTransferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para operações de banco de dados relacionadas a Transferencia.
 */
@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {

    /**
     * Lista transferências por local de origem
     */
    List<Transferencia> findByLocalOrigem(LocalEstoque localOrigem);

    /**
     * Lista transferências por local de destino
     */
    List<Transferencia> findByLocalDestino(LocalEstoque localDestino);

    /**
     * Lista transferências por status
     */
    List<Transferencia> findByStatus(StatusTransferencia status);

    /**
     * Lista últimas transferências (ordenadas por data)
     */
    @Query("SELECT t FROM Transferencia t ORDER BY t.createdAt DESC")
    List<Transferencia> findUltimasTransferencias();
}