package com.gigastore.products;

import com.gigastore.inventory.InventoryItem;
import com.gigastore.orders.Order;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "products_seq_generator", sequenceName = "products_id_seq", allocationSize = 1)
    private int id;

    @Column(
            name = "name",
            nullable = false
    )
    private String name;

    @Column(
            name = "cost",
            nullable = false
    )
    private int cost;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orders;

    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private InventoryItem inventoryItem;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
