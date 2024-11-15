package com.gigastore.orders;

import com.gigastore.orders.dtos.PlaceOrderRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderPlacementService {
    private final OrderService orderService;
    private final OrderEmailService emailService;

    public OrderPlacementService(OrderService orderService, OrderEmailService emailService) {
        this.orderService = orderService;
        this.emailService = emailService;
    }

    @Transactional
    public int placeOrder(int customerId, PlaceOrderRequest request) {
        int orderId = orderService.saveOrder(customerId, request);
        emailService.sendOrderConfirmationEmail(customerId, orderId);
        return orderId;
    }
}
