package com.example.orderprocessing.exception;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends DomainException {
    public OrderNotFoundException(String orderId) {
        super(HttpStatus.NOT_FOUND, UiCode.ORDER_NOT_FOUND, "Order not found: " + orderId);
    }
}
