package com.example.orderprocessing.exception;

public enum UiCode {
    // Generic
    INVALID_REQUEST,
    VALIDATION_FAILED,
    RESOURCE_NOT_FOUND,
    CONFLICT,
    INTERNAL_ERROR,

    // Order domain (extend as needed)
    ORDER_NOT_FOUND,
    ORDER_STATUS_TRANSITION_INVALID,
    ORDER_CANCEL_NOT_ALLOWED
}
