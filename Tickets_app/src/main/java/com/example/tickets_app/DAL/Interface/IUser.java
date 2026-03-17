package com.example.tickets_app.DAL.Interface;

import com.example.tickets_app.BE.User;

import java.sql.SQLException;

public interface IUser {
        void createUser(User user) throws SQLException;
        void editUser(int userId) throws SQLException;
        void deleteUser(int userId) throws SQLException;
        boolean validateLogin(String username, String password) throws SQLException;
        boolean emailExists(String email) throws SQLException;

}
