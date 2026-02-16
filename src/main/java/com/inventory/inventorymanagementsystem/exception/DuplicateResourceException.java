package com.inventory.inventorymanagementsystem.exception;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends BusinessException {

    public DuplicateResourceException(String resource, String field, String value) {
        super(String.format("%s com %s '%s' já existe", resource, field, value), HttpStatus.CONFLICT);
    }
}
