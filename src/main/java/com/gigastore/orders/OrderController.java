package com.gigastore.orders;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final DataAccessLayer dataAccessLayer;
    private final BackgroundService backgroundService;
    private final EmailService emailService;

    public OrderController(
            DataAccessLayer dataAccessLayer,
            BackgroundService backgroundService,
            EmailService emailService
    ) {
        this.dataAccessLayer = dataAccessLayer;
        this.backgroundService = backgroundService;
        this.emailService = emailService;
    }


    @GetMapping("/PlaceOrder/{customerId}")
    public ResponseEntity<?> placeOrder(@PathVariable int customerId, @RequestBody Order order, HttpServletRequest request) {
        if (UserSession.isUserLoggedIn(request)) {
            dataAccessLayer.addOrder(customerId, order);
            backgroundService.updateInventory(order);
            emailService.sendOrderConfirmationEmailAsync(customerId, order);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

