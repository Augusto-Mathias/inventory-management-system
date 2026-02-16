package com.inventory.inventorymanagementsystem.exception;

import org.springframework.http.HttpStatus;

public class EstoqueInsuficienteException extends BusinessException {

    public EstoqueInsuficienteException(String produtoNome, Integer disponivel, Integer solicitado) {
        super(String.format("Estoque insuficiente para %s. Disponível: %d, Solicitado: %d",
                        produtoNome, disponivel, solicitado),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
