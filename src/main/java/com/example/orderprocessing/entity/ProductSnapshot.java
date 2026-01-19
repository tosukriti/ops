package com.example.orderprocessing.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProductSnapshot {

    @Column(nullable = false, length = 64)
    private String productId;

    @Column(nullable = false, length = 200)
    private String productName;

    @Column(length = 64)
    private String sku;

    @Column(length = 100)
    private String brand;

    protected ProductSnapshot() {}

    public ProductSnapshot(String productId, String productName, String sku, String brand) {
        this.productId = productId;
        this.productName = productName;
        this.sku = sku;
        this.brand = brand;
    }

    public String getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getSku() { return sku; }
    public String getBrand() { return brand; }
}
