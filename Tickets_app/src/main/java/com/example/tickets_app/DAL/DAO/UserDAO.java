package com.example.tickets_app.DAL.DAO;

import com.example.tickets_app.BE.User;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.DB.DBConnector;
import com.example.tickets_app.DAL.Interface.IUserDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {


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

    //Implementerede sql editUser
    @Override
    public void editUser(int userId, String firstName, String lastName, String email, String phone, String role) throws ExceptionHandler {
        String sql = "UPDATE Users SET FirstName = ?, LastName = ?, Email = ?, Phone = ?, Role = ? WHERE Id = ?";

        try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, role);
            ps.setInt(6, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("editUser", e);
        }
    }

    @Override
    public void deleteUser(int userId) throws ExceptionHandler {
        String sql = "DELETE FROM Users WHERE Id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("deleteUser", e);
        }
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

    @Override
    public List<User> getAllUsers() throws ExceptionHandler {
        String sql = "SELECT Id, FirstName, LastName, Email, Phone, Password, Role, IsSeeded FROM Users WHERE IsSeeded = 0";
        List<User> users = new ArrayList<>();

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User(
                        rs.getInt("Id"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Password"),
                        rs.getString("Role"),
                        rs.getBoolean("IsSeeded")
                );
                users.add(user);
            }

        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("getAllUsers", e);
        }
        return users;
    }

    @Override
    public User getUserByEmail(String email) throws ExceptionHandler {
        String sql = "SELECT Id, FirstName, LastName, Email, Phone, Password, Role, IsSeeded FROM Users WHERE Email = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("Id"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Password"),
                        rs.getString("Role"),
                        rs.getBoolean("IsSeeded")
                );
            }

        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("getUserByEmail", e);
        }
        return null;
    }
}
