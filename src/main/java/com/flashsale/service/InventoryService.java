package com.flashsale.service;

import com.flashsale.model.Order;
import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class InventoryService {
    private final AtomicInteger stock = new AtomicInteger(100);

    public synchronized boolean buyItem(Order order) {
        if (stock.get() > 0) {
            stock.decrementAndGet();
            order.setStatus("SUCCESS");
            return true;
        } else {
            order.setStatus("SOLD_OUT");
            return false;
        }
    }

    public int getStock() { return stock.get(); }
    public void resetStock(int amount) { stock.set(amount); }
}