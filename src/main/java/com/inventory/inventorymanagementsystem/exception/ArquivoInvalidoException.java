package com.inventory.inventorymanagementsystem.exception;

import org.springframework.http.HttpStatus;

public class ArquivoInvalidoException extends BusinessException {

    public ArquivoInvalidoException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
