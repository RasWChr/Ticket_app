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

    //valider events

    public static boolean isValidEventName(String name) {
        return name != null && !name.isBlank();
    }

    public static boolean isValidDateTime(String dateTime) {
        if (dateTime == null || dateTime.isBlank()) return false;
        // Forvented format: dd-MM-yyyy HH:mm
        return dateTime.matches("^\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}$");
    }

    public static boolean isValidEventId(String eventId){
        return (eventId != null);
    }

    public static boolean isValidTicketType(String ticketType) {
        return (ticketType != null || ticketType.isBlank());
    }

    public static boolean isValidPrice(double price){
        return (price != 0);
    }

    public static boolean isValidDiscount(double discount){
        return (discount >= 0 && discount <= 100);
    }

}