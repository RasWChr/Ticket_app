package com.example.tickets_app.BLL.Interface;

import com.example.tickets_app.BLL.util.ExceptionHandler;

import java.sql.SQLException;

public interface IUserManager {
    void createUser(String firstName, String lastName, String email, String phone, String password, String role) throws ExceptionHandler;;

    boolean emailExists(String email) throws ExceptionHandler;;
}