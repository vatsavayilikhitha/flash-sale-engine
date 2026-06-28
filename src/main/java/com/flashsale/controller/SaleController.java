package com.flashsale.controller;

import com.flashsale.model.Order;
import com.flashsale.repository.OrderRepository;
import com.flashsale.service.InventoryService;
import com.flashsale.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/sale")
public class SaleController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private QueueService queueService;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/register/{userId}")
    public Map<String, String> register(@PathVariable String userId) {
        Order order = new Order(userId);
        queueService.addToQueue(order);
        Map<String, String> response = new HashMap<>();
        response.put("userId", userId);
        response.put("message", "Registered! Position: " + queueService.getQueueSize());
        return response;
    }

    @PostMapping("/buy")
    public Map<String, String> buy() {
        Map<String, String> response = new HashMap<>();
        if (queueService.isEmpty()) {
            response.put("message", "No users in queue");
            return response;
        }
        Order order = queueService.processNext();
        inventoryService.buyItem(order);
        response.put("userId", order.getUserId());
        response.put("status", order.getStatus());
        response.put("stockLeft", String.valueOf(inventoryService.getStock()));
        return response;
    }

    @GetMapping("/status")
    public Map<String, Object> status() {
        Map<String, Object> response = new HashMap<>();
        response.put("stockRemaining", inventoryService.getStock());
        response.put("usersInQueue", queueService.getQueueSize());
        return response;
    }

    @GetMapping("/history")
    public List<Order> history() {
        return orderRepository.findAll();
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        Map<String, Object> response = new HashMap<>();
        long success = orderRepository.findByStatus("SUCCESS").size();
        long soldOut = orderRepository.findByStatus("SOLD_OUT").size();
        response.put("totalOrders", success + soldOut);
        response.put("successOrders", success);
        response.put("soldOutOrders", soldOut);
        response.put("stockRemaining", inventoryService.getStock());
        return response;
    }
}