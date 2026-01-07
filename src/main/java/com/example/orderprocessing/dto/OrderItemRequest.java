package com.example.orderprocessing.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record OrderItemRequest(
        @NotBlank(message = "productId is required")
        String productId,

        @Min(value = 1, message = "quantity must be >= 1")
        int quantity,

        @NotNull(message = "unitPrice is required")
        @DecimalMin(value = "0.00", inclusive = true, message = "unitPrice must be >= 0")
        BigDecimal unitPrice
) {}
