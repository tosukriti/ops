package com.example.orderprocessing.controller;

import com.example.orderprocessing.dto.CreateOrderRequest;
import com.example.orderprocessing.dto.OrderResponse;
import com.example.orderprocessing.dto.UpdateStatusRequest;
import com.example.orderprocessing.entity.OrderStatus;
import com.example.orderprocessing.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable("id") UUID id) {
        return orderService.getOrder(id);
    }

    @GetMapping
    public List<OrderResponse> list(@RequestParam(value = "status", required = false) OrderStatus status) {
        return orderService.listOrders(status);
    }

    @PatchMapping("/{id}/status")
    public OrderResponse updateStatus(@PathVariable("id") UUID id,
                                      @Valid @RequestBody UpdateStatusRequest request) {
        return orderService.updateStatus(id, request.status());
    }

    @PostMapping("/{id}/cancel")
    public OrderResponse cancel(@PathVariable("id") UUID id) {
        return orderService.cancel(id);
    }
}
