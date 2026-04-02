package com.example.tickets_app.DAL.DAO;

import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.DB.DBConnector;
import com.example.tickets_app.DAL.Interface.ITicketDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO implements ITicketDAO {
    @Override
    public List<Ticket> getAllTickets() throws ExceptionHandler {
        String sql = "SELECT t.Id, t.EventID, t.Price, t.Discount, t.Tickettype, e.Name AS EventName " +
                "FROM Tickets t JOIN Events e ON t.EventID = e.Id";
        List<Ticket> tickets = new ArrayList<>();

            try (Connection conn = DBConnector.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Ticket ticket = new Ticket(
                            rs.getInt("Id"),
                            rs.getInt("EventID"),
                            rs.getDouble("Price"),
                            rs.getDouble("Discount"),
                            rs.getString("Tickettype")
                    );
                    ticket.setEventName(rs.getString("EventName")); // add this line
                    tickets.add(ticket);
                }

            } catch (SQLException e) {
                ExceptionHandler.handleDAOException("getAllTickets", e);
            }
            return tickets;
    }


    @Override
    public void createTicket(Ticket ticket) throws ExceptionHandler {
        String sql = "INSERT INTO Tickets (EventID, Price, Discount, Tickettype) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ticket.getEventID());
            ps.setDouble(2, ticket.getPrice());
            ps.setDouble(3, ticket.getDiscount());
            ps.setString(4, ticket.getTicketType());
            ps.executeUpdate();
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("createUser", e);
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
    public void editTicket(int ticketId, int eventId, double price, double discount, String ticketType) throws ExceptionHandler {
        String sql = "UPDATE Tickets SET EventID = ?, Price = ?, Discount = ?, Tickettype = ? WHERE ID = ?";

        try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, eventId);
            ps.setDouble(2, price);
            ps.setDouble(3, discount);
            ps.setString(4, ticketType);
            ps.setInt(5, ticketId);

            ps.executeUpdate();
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("editTicket", e);
        }
    }

}
