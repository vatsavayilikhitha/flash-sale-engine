package com.flashsale.service;

import com.flashsale.model.Order;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.Queue;

@Service
public class QueueService {
    private final Queue<Order> queue = new LinkedList<>();

    public void addToQueue(Order order) {
        queue.offer(order);
    }

    public Order processNext() {
        return queue.poll();
    }

    public boolean isEmpty() { return queue.isEmpty(); }
    public int getQueueSize() { return queue.size(); }
}