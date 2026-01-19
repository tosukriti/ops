package com.example.orderprocessing.exception;

import org.springframework.http.HttpStatus;

import java.util.Map;

public abstract class DomainException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final UiCode uiCode;
    private final Map<String, Object> meta;

    protected DomainException(HttpStatus httpStatus, UiCode uiCode, String message) {
        this(httpStatus, uiCode, message, Map.of());
    }

    protected DomainException(HttpStatus httpStatus, UiCode uiCode, String message, Map<String, Object> meta) {
        super(message);
        this.httpStatus = httpStatus;
        this.uiCode = uiCode;
        this.meta = (meta == null) ? Map.of() : Map.copyOf(meta);
    }

    public HttpStatus httpStatus() { return httpStatus; }
    public UiCode uiCode() { return uiCode; }
    public Map<String, Object> meta() { return meta; }
}
