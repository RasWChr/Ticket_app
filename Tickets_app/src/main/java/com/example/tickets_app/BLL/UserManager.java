package com.example.tickets_app.BLL;

import com.example.tickets_app.BE.User;
import com.example.tickets_app.BLL.Interface.IUserManager;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.BLL.util.PasswordUtil;
import com.example.tickets_app.DAL.Interface.IUserDAO;


public class UserManager implements IUserManager {

    private final IUserDAO userDAO;

    public UserManager(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void createUser(String firstName, String lastName, String email, String phoneNumber, String password, String role) throws ExceptionHandler {
        try {
            if (userDAO.emailExists(email)) {
                throw new IllegalArgumentException("A user with this email already exists.");
            }
            String hashedPassword = PasswordUtil.hash(password);
            User user = new User(firstName, lastName, email, phoneNumber, hashedPassword, role);
            userDAO.createUser(user);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not create user: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean emailExists(String email) throws ExceptionHandler {
        try {
            return userDAO.emailExists(email);
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not check for duplicate Email" + e.getMessage(), e);
        }
    }
}
