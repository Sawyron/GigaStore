package com.gigastore.orders;

import com.gigastore.orders.dtos.PlaceOrderRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private OrderPlacementService placementService;

    @GetMapping("/{customerId}")
    public ResponseEntity<?> placeOrder(@PathVariable int customerId, @RequestBody PlaceOrderRequest order) {
        placementService.placeOrder(customerId, order);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

