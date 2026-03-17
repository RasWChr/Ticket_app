package com.example.tickets_app.DAL.Interface;

import com.example.tickets_app.BE.User;

public interface IUser {
        void createUser(User user);
        void editUser(int userId);
        void deleteUser(int userId);
        boolean validateLogin(String username, String password);

}
