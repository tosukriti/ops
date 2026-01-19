package com.example.orderprocessing.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends DomainException {
    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, UiCode.RESOURCE_NOT_FOUND, message);
    }
}
