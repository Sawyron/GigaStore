package com.gigastore.orders;

import java.util.List;

public class DataAccessLayer {
    public void addOrder(int customerId, Order order) {
        RetailDbContext context = new RetailDbContext();
        Customer customer = context.findCustomerById(customerId);
        List<Order> customerOrders = context.findOrdersByCustomerId(customerId);

        customerOrders.add(order);

        context.addOrder(order);
        context.saveChanges();
    }
}
