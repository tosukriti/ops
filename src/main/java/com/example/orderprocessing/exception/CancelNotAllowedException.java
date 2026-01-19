package com.example.orderprocessing.exception;

import org.springframework.http.HttpStatus;

public class CancelNotAllowedException extends DomainException {
    public CancelNotAllowedException(String message) {
        super(HttpStatus.CONFLICT, UiCode.ORDER_CANCEL_NOT_ALLOWED, message);
    }
}
