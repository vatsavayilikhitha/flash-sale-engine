package com.flashsale.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String userId;
    private String status;
    private long timestamp;

    public Order(String userId) {
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
        this.status = "PENDING";
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "User: " + userId + " | Status: " + status;
    }
}