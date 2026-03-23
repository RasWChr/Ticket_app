package com.example.tickets_app.DAL.DAO;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.User;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.DB.DBConnector;
import com.example.tickets_app.DAL.Interface.IEventDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EventDAO implements IEventDAO {
    @Override
    public List<Event> getAllEvents() throws ExceptionHandler {
        String sql = "SELECT Id, Name, StartDateTime, EndDateTime, Location, LocationGuidance, Notes FROM Events";
        List<Event> events = new ArrayList<>();

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getString("StartDateTime"),
                        rs.getString("EndDateTime"),
                        rs.getString("Location"),
                        rs.getString("LocationGuidance"),
                        rs.getString("Notes")
                ));
            }

        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("getAllEvents", e);
        }
        return events;
    }

    @Override
    public List<Event> getEventsByCoordinator(int userId) throws ExceptionHandler {
        String sql = "SELECT e.Id, e.Name, e.StartDateTime, e.EndDateTime, e.Location, e.LocationGuidance, e.Notes " +
                "FROM Events e INNER JOIN EventCoordinators ec ON e.Id = ec.EventId WHERE ec.UserId = ?";
        List<Event> events = new ArrayList<>();

        try ( Connection conn = DBConnector.getConnection();
              PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getString("StartDateTime"),
                        rs.getString("EndDateTime"),
                        rs.getString("Location"),
                        rs.getString("LocationGuidance"),
                        rs.getString("Notes")
                ));
            }
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("getEventsByCoordinator", e);
        }
        return events;
    }

    // Lav og slet en event
    @Override
    public void createEvent(Event event) throws ExceptionHandler {
        String sql = "INSERT INTO Events (Name, StartDateTime, EndDateTime, Location, LocationGuidance, Notes) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, event.getName());
            ps.setString(2, event.getStartDateTime());
            ps.setString(3, event.getEndDateTime());
            ps.setString(4, event.getLocation());
            ps.setString(5, event.getLocationGuidance());
            ps.setString(6, event.getNotes());
            ps.executeUpdate();

        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("createEvent", e);
        }
    }

    @Override
    public void deleteEvent(int eventId) throws ExceptionHandler {
        String sql = "DELETE FROM Events WHERE Id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, eventId);
            ps.executeUpdate();

        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("deleteEvent", e);
        }
    }

    @Override
    public void assignCoordinator(int eventId, int userId) throws ExceptionHandler {
        String sql = "INSERT INTO EventCoordinators (EventId, UserId) VALUES (?, ?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("assignCoordinator", e);
        }

    }

    @Override
    public void removeCoordinator(int eventId, int userId) throws ExceptionHandler {
        String sql = "DELETE FROM EventCoordinators WHERE EventId = ? AND UserId = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, eventId);
            ps.setInt(2, userId);
            ps.executeUpdate();

        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("removeCoordinator", e);
        }

    }

    @Override
    public List<User> getCoordinatorForEvent(int eventId) throws ExceptionHandler {
        String sql = "SELECT u.Id, u.FirstName, u.LastName, u.Email, u.Phone, u.Password, u.Role, u.IsSeeded " +
                "FROM Users u INNER JOIN EventCoordinators ec ON u.ID = ec.UserId WHERE ec.EventId = ?";
        List<User> coordinators = new ArrayList<>();

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                coordinators.add(new User(
                        rs.getInt("Id"),
                        rs.getString("FirstName"),
                        rs.getString("LastName"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Password"),
                        rs.getString("Role"),
                        rs.getBoolean("IsSeeded")
                ));
            }
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("getCoordinatorForEvent", e);
        }
        return coordinators;

    }

    @Override
    public boolean isCoordinatorAssigned(int eventId, int userId) throws ExceptionHandler {
        String sql = "SELECT COUNT(*) FROM EventCoordinators WHERE EventId = ? AND UserId = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, eventId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
        }
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("isCoordinatorAssigned", e);
        }
        return false;
    }
}
