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
        String sql = "SELECT Id, Event Name, Price, Image, Ticket type  FROM Tickets";
        List<Ticket> tickets = new ArrayList<>();

            try (Connection conn = DBConnector.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Ticket ticket = new Ticket(
                            rs.getInt("Id"),
                            rs.getString("Event name"),
                            rs.getInt("Price"),
                            rs.getString("Image"),
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

    }

    @Override
    public void deleteTicket(int ticketId) throws ExceptionHandler {

    }

    @Override
    public void editTicket(int ticketId, String eventName, int price, String image, String ticketType) {

    }

}
