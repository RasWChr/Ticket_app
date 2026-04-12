package com.example.tickets_app.DAL.DAO;

import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.DB.DBConnector;
import com.example.tickets_app.DAL.Interface.ITicketDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO implements ITicketDAO {
    @Override
    public List<Ticket> getAllTickets() throws ExceptionHandler {
        // LEFT JOIN so global tickets (EventID IS NULL) are still returned
        String sql = """
                SELECT t.ID, t.[EventID] AS EventID, t.Price, t.Discount,
                       t.[Tickettype] AS Tickettype, t.IsGlobal,
                       e.Name AS EventName
                FROM Tickets t
                LEFT JOIN Events e ON t.[EventID] = e.Id
                """;
        List<Ticket> tickets = new ArrayList<>();
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            tickets = mapResults(ps.executeQuery());
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("getAllTickets", e);
        }
        return tickets;
    }

    @Override
    public List<Ticket> getTicketsByEventId(int eventId) throws ExceptionHandler {
        // Returns tickets locked to this event AND global tickets
        String sql = """
                SELECT t.ID, t.[EventID] AS EventID, t.Price, t.Discount,
                       t.[Tickettype] AS Tickettype, t.IsGlobal,
                       e.Name AS EventName
                FROM Tickets t
                LEFT JOIN Events e ON t.[EventID] = e.Id
                WHERE t.[EventID] = ? OR t.IsGlobal = 1
                """;
        List<Ticket> tickets = new ArrayList<>();
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            tickets = mapResults(ps.executeQuery());
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("getTicketsByEventId", e);
        }
        return tickets;
    }


    @Override
    public void createTicket(Ticket ticket) throws ExceptionHandler {
        String sql = "INSERT INTO Tickets ([EventID], Price, Discount, [Tickettype], IsGlobal) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (ticket.isGlobal() || ticket.getEventID() == null) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, ticket.getEventID());
            }
            ps.setDouble(2, ticket.getPrice());
            ps.setDouble(3, ticket.getDiscount());
            ps.setString(4, ticket.getTicketType());
            ps.setBoolean(5, ticket.isGlobal());
            ps.executeUpdate();
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("createTicket", e);
        }
    }

    @Override
    public void deleteTicket(int ticketId) throws ExceptionHandler {
        String sql = "DELETE FROM Tickets WHERE ID = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketId);
            ps.executeUpdate();
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("deleteTicket", e);
        }
    }

    @Override
    public void editTicket(int ticketId, Integer eventId, double price, double discount,
                           String ticketType, boolean isGlobal) throws ExceptionHandler {
        String sql = "UPDATE Tickets SET [EventID] = ?, Price = ?, Discount = ?, [Tickettype] = ?, IsGlobal = ? WHERE ID = ?";
        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (isGlobal || eventId == null) {
                ps.setNull(1, Types.INTEGER);
            } else {
                ps.setInt(1, eventId);
            }
            ps.setDouble(2, price);
            ps.setDouble(3, discount);
            ps.setString(4, ticketType);
            ps.setBoolean(5, isGlobal);
            ps.setInt(6, ticketId);
            ps.executeUpdate();
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("editTicket", e);
        }
    }


    private List<Ticket> mapResults(ResultSet rs) throws SQLException {
        List<Ticket> list = new ArrayList<>();
        while (rs.next()) {
            int rawEventId = rs.getInt("EventID");
            Integer eventId = rs.wasNull() ? null : rawEventId;
            boolean isGlobal = rs.getBoolean("IsGlobal");

            Ticket ticket = new Ticket(
                    rs.getInt("ID"),
                    eventId,
                    rs.getDouble("Price"),
                    rs.getDouble("Discount"),
                    rs.getString("Tickettype"),
                    isGlobal
            );
            ticket.setEventName(rs.getString("EventName"));
            list.add(ticket);
        }
        return list;
    }
}