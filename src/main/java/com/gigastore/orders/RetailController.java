package com.gigastore.orders;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/retail")
public class RetailController {

    private DataAccessLayer dataAccessLayer;
    private BackgroundService backgroundService;
    private EmailService emailService;

    public RetailController() {
        this.dataAccessLayer = new DataAccessLayer();
        this.backgroundService = new BackgroundService();
        this.emailService = new EmailService();
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

