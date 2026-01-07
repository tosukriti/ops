package com.example.orderprocessing.scheduler;

import com.example.orderprocessing.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusScheduler {

    private static final Logger log = LoggerFactory.getLogger(OrderStatusScheduler.class);

    private final OrderRepository orderRepository;

    public OrderStatusScheduler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    @Scheduled(fixedRate = 5 * 60 * 1000L) // every 5 minutes
    public void promotePendingOrders() {
        int updated = orderRepository.bulkPromotePendingToProcessing();
        if (updated > 0) {
            log.info("Auto-promoted {} orders from PENDING -> PROCESSING", updated);
        }
    }
}
