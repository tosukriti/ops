package com.example.orderprocessing.scheduler;

import com.example.orderprocessing.entity.Order;
import com.example.orderprocessing.entity.OrderItem;
import com.example.orderprocessing.entity.OrderStatus;
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
        Order pending = new Order();
        pending.setStatus(OrderStatus.PENDING);
        OrderItem it = new OrderItem();
        it.setProductId("SKU-1");
        it.setQuantity(1);
        it.setUnitPrice(new BigDecimal("10.00"));
        pending.addItem(it);

        Order shipped = new Order();
        shipped.setStatus(OrderStatus.SHIPPED);
        OrderItem it2 = new OrderItem();
        it2.setProductId("SKU-2");
        it2.setQuantity(1);
        it2.setUnitPrice(new BigDecimal("20.00"));
        shipped.addItem(it2);

        orderRepository.save(pending);
        orderRepository.save(shipped);

        scheduler.promotePendingOrders();

        long processingCount = orderRepository.findAllByStatus(OrderStatus.PROCESSING).size();
        long shippedCount = orderRepository.findAllByStatus(OrderStatus.SHIPPED).size();

        assertEquals(1, processingCount);
        assertEquals(1, shippedCount);
    }
}
