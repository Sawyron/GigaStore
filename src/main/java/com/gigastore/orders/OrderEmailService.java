package com.gigastore.orders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestClient;

import java.util.HashMap;

public class OrderEmailService {
    private static final Logger log = LoggerFactory.getLogger(OrderEmailService.class);

    private final RestClient client;

    public OrderEmailService(RestClient client) {
        this.client = client;
    }

    @Async
    public void sendOrderConfirmationEmail(int customerId, int orderId) {
        var email = new HashMap<String, String>();
        email.put("to", "customer %s@example.com".formatted(customerId));
        email.put("subject", "Order Confirmation");
        email.put("body", "Order confirmation for order ID: %s".formatted(orderId));
        ResponseEntity<String> responseEntity = client.post()
                .uri("/send")
                .body(email)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .toEntity(String.class);
        if (responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
            log.info("successfully send email. Response: {}", responseEntity);
        } else {
            log.error("Failed to send order confirmation email. Response Code: {}", responseEntity.getStatusCode());
        }
    }
}
