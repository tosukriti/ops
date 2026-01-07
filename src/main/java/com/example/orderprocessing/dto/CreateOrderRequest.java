package com.example.orderprocessing.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CreateOrderRequest(
        @NotEmpty(message = "items must not be empty")
        @Valid
        List<OrderItemRequest> items
) {}
