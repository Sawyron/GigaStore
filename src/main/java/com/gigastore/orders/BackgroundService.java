package com.gigastore.orders;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;

public class BackgroundService {
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
