package com.example.orderprocessing.dto;

import com.example.orderprocessing.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest(
        @NotNull(message = "status is required")
        OrderStatus status
) {}
