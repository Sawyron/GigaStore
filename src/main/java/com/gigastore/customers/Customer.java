package com.gigastore.customers;

import com.gigastore.orders.Order;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customers_seq_generator")
    @SequenceGenerator(name = "customers_seq_generator", sequenceName = "customer_id_seq", allocationSize = 1)
    private int id;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
