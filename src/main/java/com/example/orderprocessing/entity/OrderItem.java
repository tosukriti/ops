package com.example.orderprocessing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Embedded
    private ProductSnapshot product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal unitPrice;

    @PrePersist
    void prePersist() {
        this.id = (this.id == null) ? UUID.randomUUID() : this.id;
    }

    public UUID getId() { return id; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public ProductSnapshot getProduct() { return product; }
    public void setProduct(ProductSnapshot product) { this.product = product; }

    // Convenience getters so your code changes minimally
    public String getProductId() { return product != null ? product.getProductId() : null; }
    public String getProductName() { return product != null ? product.getProductName() : null; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}
