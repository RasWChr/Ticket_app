package com.example.tickets_app.DAL.DAO;

import com.example.tickets_app.BE.User;
import com.example.tickets_app.DAL.Interface.IUser;

public class UserDAO implements IUser {


    @Override
    public void createUser(User user) {

    }

    @Override
    public void editUser(int userId) {

    }

    @Override
    public void deleteUser(int userId) {

    }

    @Override
    public boolean validateLogin(String username, String password) {
        return false;
    }
}
