package com.gigastore;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.CompletableFuture;

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

class UserSession {
    public static List<String> userNames = new ArrayList<>();

    public static void saveUser(String userName) {
        userNames.add(userName);
    }

    public static boolean isUserLoggedIn(HttpServletRequest request) {
        String userName = request.getHeader("User");
        return userNames.contains(userName);
    }
}

class DataAccessLayer {
    public void addOrder(int customerId, Order order) {
        RetailDbContext context = new RetailDbContext();
        Customer customer = context.findCustomerById(customerId);
        List<Order> customerOrders = context.findOrdersByCustomerId(customerId);

        customerOrders.add(order);

        context.addOrder(order);
        context.saveChanges();
    }
}

class BackgroundService {
    public void updateInventory(Order order) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/retaildb", "user", "password");
            connection.setAutoCommit(true);

            Date currentDate = new Date();
            String query = "UPDATE Inventory SET Quantity = Quantity - " + order.getQuantity() +
                           ", LastUpdated = '" + currentDate.toString() +
                           "' WHERE ProductId = " + order.getProductId();

            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);

            connection.close();
        } catch (Exception e) {
            System.out.println("Error updating inventory: " + e.getMessage());
        }
    }
}

class EmailService {
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

class RetailDbContext {
    public Customer findCustomerById(int customerId) {
        // Имитация поиска клиента в базе данных
        return new Customer();
    }

    public List<Order> findOrdersByCustomerId(int customerId) {
        // Имитация поиска заказов клиента в базе данных
        return new ArrayList<>();
    }

    public void addOrder(Order order) {
        // Имитация добавления заказа в базу данных
    }

    public void saveChanges() {
        // Имитация сохранения изменений в базе данных
    }
}

class Customer {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

class Order {
    private Object quantity;
    private Object productId;
    private Object id;
    private int customerId;

    public Object getQuantity() {
        return quantity;
    }

    public void setQuantity(Object quantity) {
        this.quantity = quantity;
    }

    public Object getProductId() {
        return productId;
    }

    public void setProductId(Object productId) {
        this.productId = productId;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}

class Product {

}

class Inventory {

}
