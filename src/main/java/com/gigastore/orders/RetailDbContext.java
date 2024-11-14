package com.gigastore.orders;

import java.util.ArrayList;
import java.util.List;

public class RetailDbContext {
    public Customer findCustomerById(int customerId) {
        // Имитация поиска клиента в базе данных
        return new Customer();
    }

    public List<Order> findOrdersByCustomerId(int customerId) {
        // Имитация поиска заказов клиента в базе данных
        return new ArrayList<>();
    }

    public void addOrder(Order order) {
        // Имитация добавления заказа в базу данных
    }

    public void saveChanges() {
        // Имитация сохранения изменений в базе данных
    }
}
