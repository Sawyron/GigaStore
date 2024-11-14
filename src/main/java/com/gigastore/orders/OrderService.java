package com.gigastore.orders;

import com.gigastore.customers.Customer;
import com.gigastore.customers.CustomerRepository;
import com.gigastore.inventory.InventoryService;
import com.gigastore.orders.dtos.PlaceOrderRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final InventoryService inventoryService;

    public OrderService(
            OrderRepository orderRepository,
            CustomerRepository customerRepository,
            InventoryService inventoryService
    ) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public int saveOrder(int customerId, PlaceOrderRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "customer with id %s is not found".formatted(customerId))
                );
        var order = new Order();
        order.setQuantity(request.quantity());
        order.setCustomer(customer);
        orderRepository.save(order);
        inventoryService.updateInventory(request.productId(), request.quantity());
        return order.getId();
    }
}