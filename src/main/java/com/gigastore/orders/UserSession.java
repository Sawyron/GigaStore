package com.gigastore.orders;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

public class UserSession {
    public static List<String> userNames = new ArrayList<>();

    public static void saveUser(String userName) {
        userNames.add(userName);
    }

    public static boolean isUserLoggedIn(HttpServletRequest request) {
        String userName = request.getHeader("User");
        return userNames.contains(userName);
    }
}
