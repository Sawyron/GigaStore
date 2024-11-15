package com.gigastore.orders;

import com.gigastore.orders.dtos.PlaceOrderRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderPlacementService placementService;

    public OrderController(OrderPlacementService placementService) {
        this.placementService = placementService;
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<Integer> placeOrder(
            @PathVariable int customerId,
            @RequestBody @Valid PlaceOrderRequest order
    ) {
        int orderId = placementService.placeOrder(customerId, order);
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }
}

