package com.example.tickets_app.BLL.util;

public class ExceptionHandler extends RuntimeException {

    public ExceptionHandler(String message) {
        super(message);
    }

    public ExceptionHandler(String message, Throwable cause) {
        super(message, cause);
    }

    public static void handleDAOException(String operation, Exception e) throws ExceptionHandler {
        throw new ExceptionHandler("Database error during " + operation + ": " + e.getMessage(), e);
    }
}