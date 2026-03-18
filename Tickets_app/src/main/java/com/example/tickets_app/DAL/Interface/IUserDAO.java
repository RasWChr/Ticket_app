package com.example.tickets_app.DAL.Interface;

import com.example.tickets_app.BE.User;
import com.example.tickets_app.BLL.util.ExceptionHandler;

public interface IUserDAO {
        void createUser(User user) throws ExceptionHandler;
        void editUser(int userId) throws ExceptionHandler;
        void deleteUser(int userId) throws ExceptionHandler;
        boolean validateLogin(String username, String password) throws ExceptionHandler;
        boolean emailExists(String email) throws ExceptionHandler;

}
