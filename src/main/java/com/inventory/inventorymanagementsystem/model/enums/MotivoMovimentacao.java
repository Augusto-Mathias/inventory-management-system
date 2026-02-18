package com.inventory.inventorymanagementsystem.model.enums;

/**
 * Motivos/razões para movimentações de estoque.
 *
 * Permite rastrear exatamente por que cada movimentação foi feita,
 * facilitando relatórios e auditoria.
 */
public enum MotivoMovimentacao {

    // ========== ENTRADAS ==========

    /**
     * Compra de produtos de fornecedor
     */
    COMPRA_FORNECEDOR,

    /**
     * Importação de produtos (China, exterior)
     */
    IMPORTACAO,

    /**
     * Devolução de cliente em bom estado
     */
    DEVOLUCAO_BOM_ESTADO,

    /**
     * Produto que voltou do reparo funcionando
     */
    PRODUTO_REPARADO,

    /**
     * Ajuste manual de estoque para aumentar quantidade
     */
    ENTRADA_AJUSTE_ESTOQUE,

    /**
     * Reclassificação de produto entre categorias/status
     */
    ENTRADA_RECLASSIFICACAO,

    /**
     * Recebimento de transferência entre locais
     */
    RECEBIMENTO_TRANSFERENCIA,

    /**
     * Volta de produto do reparo (após conserto)
     */
    VOLTA_REPARO,

    /**
     * Entrada no saldão (produto com defeito estético)
     */
    ENTRADA_SALDAO,

    /**
     * Produto devolvido pelo cliente (aguardando inspeção)
     */
    DEVOLUCAO_CLIENTE,

    // ========== SAÍDAS ==========

    /**
     * Venda para cliente final
     */
    VENDA,

    /**
     * Perda de produto (furto, extravio, validade vencida)
     */
    PERDA,

    /**
     * Quebra de produto durante manuseio/transporte
     */
    QUEBRA,

    /**
     * Devolução de produto com defeito para fornecedor
     */
    DEVOLUCAO_FORNECEDOR,

    /**
     * Envio de produto para reparo/conserto externo
     */
    ENVIO_REPARO,

    /**
     * Saída para reclassificar produto
     */
    SAIDA_RECLASSIFICACAO,

    /**
     * Descarte de produto com defeito irrecuperável
     */
    DESCARTE_ESTOQUE,

    /**
     * Envio de transferência para outro local
     */
    ENVIO_TRANSFERENCIA,

    /**
     * Saída para fulfillment (ML Full, Shopee, etc)
     */
    ENVIO_FULFILLMENT,

    /**
     * Ajuste manual de estoque para diminuir quantidade
     */
    SAIDA_AJUSTE_ESTOQUE,

    /**
     * Saída para o saldão
     */
    SAIDA_SALDAO,

    /**
     * Reparo de estoque (produto vai para manutenção)
     */
    REPARO_ESTOQUE,

    /**
     * Amostra grátis enviada para cliente
     */
    AMOSTRA_GRATIS,

    /**
     * Brinde/Bonificação
     */
    BRINDE,

    /**
     * Uso interno da empresa
     */
    USO_INTERNO,

    // ========== TRANSFERÊNCIAS ==========

    /**
     * Transferência entre filiais
     */
    TRANSFERENCIA_FILIAL,

    /**
     * Reposição de estoque entre locais
     */
    REPOSICAO_LOCAL,

    // ========== AJUSTES ==========

    /**
     * Ajuste após inventário físico
     */
    AJUSTE_INVENTARIO,

    /**
     * Correção de erro de lançamento anterior
     */
    CORRECAO_LANCAMENTO;
}