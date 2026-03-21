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

    @Override
    public void assignCoordinator(int eventId, int userId) throws ExceptionHandler {

    }

    @Override
    public void removeCoordinator(int eventId, int userId) throws ExceptionHandler {

    }

    @Override
    public List<User> getCoordinatorForEvent(int eventId) throws ExceptionHandler {
        return List.of();
    }

    @Override
    public boolean isCoordinatorAssigned(int eventId, int userId) throws ExceptionHandler {
        return false;
    }
}
