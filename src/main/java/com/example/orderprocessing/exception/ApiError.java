package com.example.orderprocessing.exception;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record ApiError(
        OffsetDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        String traceId,
        String uiCode,
        List<FieldViolation> violations,
        Map<String, Object> meta
) {
    public record FieldViolation(String field, String message) {}
}
