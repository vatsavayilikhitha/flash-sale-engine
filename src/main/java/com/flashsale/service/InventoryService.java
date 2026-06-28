package com.flashsale.service;

import com.flashsale.model.Order;
import com.flashsale.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class InventoryService {
    private final AtomicInteger stock = new AtomicInteger(100);

    @Autowired
    private OrderRepository orderRepository;

    public synchronized boolean buyItem(Order order) {
        if (stock.get() > 0) {
            stock.decrementAndGet();
            order.setStatus("SUCCESS");
        } else {
            order.setStatus("SOLD_OUT");
        }
        orderRepository.save(order);
        return order.getStatus().equals("SUCCESS");
    }

    public int getStock() { return stock.get(); }
    public void resetStock(int amount) { stock.set(amount); }
}