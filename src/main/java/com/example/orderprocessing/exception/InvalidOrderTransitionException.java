package com.example.orderprocessing.exception;

import org.springframework.http.HttpStatus;

public class InvalidOrderTransitionException extends DomainException {
    public InvalidOrderTransitionException(String message) {
        super(HttpStatus.CONFLICT, UiCode.ORDER_STATUS_TRANSITION_INVALID, message);
    }
}
