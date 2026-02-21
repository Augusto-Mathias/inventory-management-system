package com.inventory.inventorymanagementsystem.model.enums;

/**
 * Status de uma transferência entre locais de estoque.
 */
public enum StatusTransferencia {

    /**
     * Transferência criada, aguardando processamento
     */
    PENDENTE,

    /**
     * Transferência em andamento (produtos sendo movidos)
     */
    EM_ANDAMENTO,

    /**
     * Transferência concluída com sucesso
     */
    CONCLUIDA,

    /**
     * Transferência cancelada (produtos não foram movidos)
     */
    CANCELADA;
}