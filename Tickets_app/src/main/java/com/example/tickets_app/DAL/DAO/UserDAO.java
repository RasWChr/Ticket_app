package com.example.tickets_app.DAL.DAO;

import com.example.tickets_app.BE.User;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.DB.DBConnector;
import com.example.tickets_app.DAL.Interface.IUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO implements IUser {


    @Override
    public void createUser(User user) {
        String sql = "INSERT INTO Users (FirstName, LastName, Email, Phone, Password, Role) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhoneNumber());
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getRole());
            ps.executeUpdate();
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("createUser", e);
        }

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

    @Override
    public boolean emailExists(String email) throws ExceptionHandler {
        String sql = "SELECT COUNT(*) FROM Users WHERE Email = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("emailExists", e);
        }
        return false;
    }
}
