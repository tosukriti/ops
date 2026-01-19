package com.example.orderprocessing.scheduler;

import com.example.orderprocessing.entity.Order;
import com.example.orderprocessing.entity.OrderItem;
import com.example.orderprocessing.entity.OrderStatus;
import com.example.orderprocessing.entity.ProductSnapshot;
import com.example.orderprocessing.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OrderStatusSchedulerIT {

    @Autowired OrderRepository orderRepository;
    @Autowired OrderStatusScheduler scheduler;

    @Test
    void schedulerPromotesPendingToProcessing() {
        // --- PENDING order ---
        Order pending = new Order();
        pending.setStatus(OrderStatus.PENDING);

        OrderItem it = new OrderItem();
        it.setProduct(new ProductSnapshot("SKU-1", "Apple", "SKU-1", "Brand-A"));
        it.setQuantity(1);
        it.setUnitPrice(new BigDecimal("10.00"));
        pending.addItem(it);

        // --- SHIPPED order (should not change) ---
        Order shipped = new Order();
        shipped.setStatus(OrderStatus.SHIPPED);

        OrderItem it2 = new OrderItem();
        it2.setProduct(new ProductSnapshot("SKU-2", "Banana", "SKU-2", "Brand-B"));
        it2.setQuantity(1);
        it2.setUnitPrice(new BigDecimal("20.00"));
        shipped.addItem(it2);

        orderRepository.save(pending);
        orderRepository.save(shipped);

        // Act
        scheduler.promotePendingOrders();

        // Assert
        long processingCount = orderRepository.findAllByStatus(OrderStatus.PROCESSING).size();
        long shippedCount = orderRepository.findAllByStatus(OrderStatus.SHIPPED).size();

        assertEquals(1, processingCount);
        assertEquals(1, shippedCount);
    }
}
