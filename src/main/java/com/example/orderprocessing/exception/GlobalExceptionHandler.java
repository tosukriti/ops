package com.example.orderprocessing.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ---- Domain exceptions (preferred) ----
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomain(DomainException ex, HttpServletRequest req) {
        // warn-level for domain errors (expected business failures)
        log.warn("Domain error: uiCode={}, message={}, path={}", ex.uiCode(), ex.getMessage(), req.getRequestURI());

        return build(
                ex.httpStatus(),
                ex.uiCode(),
                ex.getMessage(),
                req,
                List.of(),
                ex.meta()
        );
    }

    // ---- Validation: @Valid body ----
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                 HttpServletRequest req) {
        List<ApiError.FieldViolation> violations = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ApiError.FieldViolation(err.getField(), err.getDefaultMessage()))
                .toList();

        return build(
                HttpStatus.BAD_REQUEST,
                UiCode.VALIDATION_FAILED,
                "Validation failed",
                req,
                violations,
                Map.of()
        );
    }

    // ---- Validation: query/path binding ----
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiError> handleBindException(BindException ex, HttpServletRequest req) {
        List<ApiError.FieldViolation> violations = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> new ApiError.FieldViolation(err.getField(), err.getDefaultMessage()))
                .toList();

        return build(
                HttpStatus.BAD_REQUEST,
                UiCode.VALIDATION_FAILED,
                "Validation failed",
                req,
                violations,
                Map.of()
        );
    }

    // ---- Malformed JSON / wrong enum ----
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return build(
                HttpStatus.BAD_REQUEST,
                UiCode.INVALID_REQUEST,
                "Malformed JSON request body",
                req,
                List.of(),
                Map.of()
        );
    }

    // ---- Missing query param ----
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParam(MissingServletRequestParameterException ex, HttpServletRequest req) {
        return build(
                HttpStatus.BAD_REQUEST,
                UiCode.INVALID_REQUEST,
                "Missing request parameter: " + ex.getParameterName(),
                req,
                List.of(),
                Map.of("parameter", ex.getParameterName())
        );
    }

    // ---- Wrong HTTP method ----
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        return build(
                HttpStatus.METHOD_NOT_ALLOWED,
                UiCode.INVALID_REQUEST,
                "Method not allowed",
                req,
                List.of(),
                Map.of("supported", ex.getSupportedHttpMethods())
        );
    }

    // ---- Fallback ----
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleOther(Exception ex, HttpServletRequest req) {
        // error-level for unexpected exceptions
        log.error("Unexpected error at path={}", req.getRequestURI(), ex);

        return build(
                HttpStatus.INTERNAL_SERVER_ERROR,
                UiCode.INTERNAL_ERROR,
                "Unexpected error occurred. Please contact support with traceId.",
                req,
                List.of(),
                Map.of()
        );
    }

    private ResponseEntity<ApiError> build(
            HttpStatus status,
            UiCode uiCode,
            String message,
            HttpServletRequest req,
            List<ApiError.FieldViolation> violations,
            Map<String, Object> meta
    ) {
        String traceId = MDC.get(TraceIdFilter.TRACE_ID);

        ApiError body = new ApiError(
                OffsetDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                req.getRequestURI(),
                traceId,
                uiCode.name(),
                violations,
                meta
        );
        return ResponseEntity.status(status).body(body);
    }
}
