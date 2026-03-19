package com.example.tickets_app.BLL.util;

public class ValidationUtil {

    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isBlank()) return true; // phone is optional
        return phone.matches("^[+]?[0-9]{7,15}$");
    }
}