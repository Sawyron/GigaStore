package com.gigastore.orders.dtos;

import jakarta.validation.constraints.Positive;

public record PlaceOrderRequest(@Positive int productId, @Positive int quantity) {
}
