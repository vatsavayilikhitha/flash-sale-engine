package com.flashsale.model;

public class Order {
    private String userId;
    private String status;
    private long timestamp;

    public Order(String userId) {
        this.userId = userId;
        this.timestamp = System.currentTimeMillis();
        this.status = "PENDING";
    }

    public String getUserId() { return userId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "User: " + userId + " | Status: " + status;
    }
}