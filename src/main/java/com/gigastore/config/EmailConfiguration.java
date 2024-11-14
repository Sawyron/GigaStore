package com.gigastore.config;

import com.gigastore.orders.OrderEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class EmailConfiguration {
    @Bean
    public OrderEmailService orderEmailService(@Value("${order-confirmation.host}") String host) {
        return new OrderEmailService(orderRestClient(host));
    }

    @Bean
    public RestClient orderRestClient(String host) {
        return RestClient.builder()
                .baseUrl(host)
                .build();
    }
}
