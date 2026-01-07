package com.example.orderprocessing.repository;

import com.example.orderprocessing.entity.Order;
import com.example.orderprocessing.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByStatus(OrderStatus status);

    @Modifying
    @Query("update Order o set o.status = com.example.orderprocessing.entity.OrderStatus.PROCESSING " +
            "where o.status = com.example.orderprocessing.entity.OrderStatus.PENDING")
    int bulkPromotePendingToProcessing();
}
