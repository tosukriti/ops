package com.example.orderprocessing.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends DomainException {
    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, UiCode.CONFLICT, message);
    }
}
