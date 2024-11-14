package com.gigastore.inventory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, Integer> {
    Optional<InventoryItem> findByProductId(int productId);
}
