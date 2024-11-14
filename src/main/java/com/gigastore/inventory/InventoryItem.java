package com.gigastore.inventory;

import com.gigastore.products.Product;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
public class InventoryItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_seq_generator")
    @SequenceGenerator(name = "inventory_seq_generator", sequenceName = "inventory_id_seq", allocationSize = 1)
    private int id;

    @Column(
            name = "quantity",
            nullable = false
    )
    private int quantity;

    @Column(
            name = "last_updated",
            nullable = false
    )
    @CreatedDate
    private LocalDateTime lastUpdated;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
