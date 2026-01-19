package com.example.orderprocessing.service;

import com.example.orderprocessing.dto.CreateOrderRequest;
import com.example.orderprocessing.dto.OrderItemRequest;
import com.example.orderprocessing.entity.Order;
import com.example.orderprocessing.entity.OrderStatus;
import com.example.orderprocessing.exception.CancelNotAllowedException;
import com.example.orderprocessing.exception.InvalidOrderTransitionException;
import com.example.orderprocessing.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Test
    void createOrder_defaultsToPending_andPersistsItems() {
        OrderRepository repo = mock(OrderRepository.class);
        OrderService service = new OrderService(repo);

        // Simulate what JPA @PrePersist would do (unit tests don't trigger it)
        when(repo.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);

            // Mimic @PrePersist behavior for status at least
            if (o.getStatus() == null) {
                o.setStatus(OrderStatus.PENDING);
            }

            // Optional ID reflect set (if you rely on it somewhere)
            try {
                var idField = Order.class.getDeclaredField("id");
                idField.setAccessible(true);
                if (idField.get(o) == null) idField.set(o, UUID.randomUUID());
            } catch (Exception ignored) {}

            return o;
        });

        CreateOrderRequest req = new CreateOrderRequest(List.of(
                new OrderItemRequest("SKU-1", 2, new BigDecimal("10.00"))
        ));

        var resp = service.createOrder(req);

        assertNotNull(resp);
        assertEquals(OrderStatus.PENDING, resp.status());
        assertEquals(1, resp.items().size());

        // Response should still show SKU via snapshot
        assertEquals("SKU-1", resp.items().get(0).productId());
        assertEquals(2, resp.items().get(0).quantity());
        assertEquals(new BigDecimal("10.00"), resp.items().get(0).unitPrice());

        // Validate we persisted the aggregate correctly
        ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
        verify(repo).save(captor.capture());

        Order saved = captor.getValue();
        assertNotNull(saved);
        assertEquals(1, saved.getItems().size());

        var savedItem = saved.getItems().get(0);

        // Product id comes from snapshot now
        assertEquals("SKU-1", savedItem.getProductId());
        assertNotNull(savedItem.getProduct());
        assertEquals("SKU-1", savedItem.getProduct().getProductId());

        assertEquals(2, savedItem.getQuantity());
        assertEquals(new BigDecimal("10.00"), savedItem.getUnitPrice());
    }

    @Test
    void updateStatus_rejectsJumpTransition() {
        OrderRepository repo = mock(OrderRepository.class);
        OrderService service = new OrderService(repo);

        UUID id = UUID.randomUUID();
        Order o = new Order();
        o.setStatus(OrderStatus.PENDING);

        when(repo.findById(id)).thenReturn(Optional.of(o));

        assertThrows(InvalidOrderTransitionException.class,
                () -> service.updateStatus(id, OrderStatus.SHIPPED));

        verify(repo, never()).save(any());
    }

    @Test
    void cancel_onlyAllowedWhenPending() {
        OrderRepository repo = mock(OrderRepository.class);
        OrderService service = new OrderService(repo);

        UUID id = UUID.randomUUID();
        Order o = new Order();
        o.setStatus(OrderStatus.PROCESSING);

        when(repo.findById(id)).thenReturn(Optional.of(o));

        assertThrows(CancelNotAllowedException.class, () -> service.cancel(id));
    }
}
