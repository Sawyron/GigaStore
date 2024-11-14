package com.gigastore.orders;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class EmailService {
    public void sendOrderConfirmationEmailAsync(int customerId, Order order) {
        CompletableFuture.runAsync(() -> {
            try {
                Map<String, String> email = new HashMap<>();
                email.put("to", "customer" + customerId + "@example.com");
                email.put("subject", "Order Confirmation");
                email.put("body", "Order confirmation for order ID: " + order.getId() + ".");

                // Подготовка данных для отправки
                StringBuilder postData = new StringBuilder();
                for (Map.Entry<String, String> entry : email.entrySet()) {
                    if (postData.length() != 0) postData.append('&');
                    postData.append(entry.getKey())
                            .append('=')
                            .append(entry.getValue());
                }
                byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                // Создание и настройка соединения
                URL url = new URL("https://email-api.example.com/send");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

                // Отправка данных
                OutputStream os = conn.getOutputStream();
                os.write(postDataBytes);
                os.flush();
                os.close();

                // Чтение ответа
                int responseCode = conn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    throw new Exception("Failed to send order confirmation email. Response Code: " + responseCode);
                }

                // Чтение тела ответа
                InputStream is = conn.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Закрытие соединения
                conn.disconnect();

            } catch (Exception ex) {
                System.out.println("Error sending email: " + ex.getMessage());
            }
        });
    }
}
