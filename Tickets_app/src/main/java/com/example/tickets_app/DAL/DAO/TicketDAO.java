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
        String sql = "SELECT Id, Event ID, Price, Ticket type  FROM Tickets";
        List<Ticket> tickets = new ArrayList<>();

            try (Connection conn = DBConnector.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Ticket ticket = new Ticket(
                            rs.getInt("Id"),
                            rs.getInt("Event ID"),
                            rs.getInt("Price"),
                            rs.getString("Ticket type")
                    );
                    tickets.add(ticket);
                }

            } catch (SQLException e) {
                ExceptionHandler.handleDAOException("getAllTickets", e);
            }
            return tickets;
    }


    @Override
    public void createTicket(Ticket ticket) throws ExceptionHandler {
        String sql = "INSERT INTO Tickets (Event ID, Price, Discount, Ticket type) VALUES (?, ?, ?, ?)";

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
        String sql = "DELETE FROM Tickets WHERE Id = ?";

        try (Connection conn = DBConnector.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ticketId);
            ps.executeUpdate();

        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("deleteUser", e);
        }
    }

    @Override
    public void editTicket(int ticektId, int eventId, double price, double discount, String ticketType) throws ExceptionHandler {
        String sql = "UPDATE Ticekts SET Event ID = ?, Price = ?, Discount = ?, Ticket type = ?";

        try (Connection conn = DBConnector.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, eventId);
            ps.setDouble(2, price);
            ps.setDouble(3, discount);
            ps.setString(4, ticketType);

            ps.executeUpdate();
        } catch (SQLException e) {
            ExceptionHandler.handleDAOException("editUser", e);
        }
    }

}
