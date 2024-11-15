package com.gigastore.orders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

public class OrderEmailService {
    private static final Logger log = LoggerFactory.getLogger(OrderEmailService.class);

    private final RestClient client;

    public OrderEmailService(RestClient client) {
        this.client = client;
    }

    @Async
    public void sendOrderConfirmationEmail(int customerId, int orderId) {
        try {
            ResponseEntity<String> responseEntity = sendRequest(customerId, orderId);
            if (responseEntity.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
                log.info("successfully send email. Response: {}", responseEntity);
            } else {
                log.error("Failed to send order confirmation email. Response Code: {}", responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            // TODO handle sending error
            log.error("cannot send order confirmation request");
            throw new RuntimeException(e);
        }
    }

    private ResponseEntity<String> sendRequest(int customerId, int orderId) {
        var email = new LinkedMultiValueMap<String, String>();
        email.add("to", "customer %s@example.com".formatted(customerId));
        email.add("subject", "Order Confirmation");
        email.add("body", "Order confirmation for order ID: %s".formatted(orderId));
        return client.post()
                .uri("/send")
                .body(email)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .toEntity(String.class);
    }
}
