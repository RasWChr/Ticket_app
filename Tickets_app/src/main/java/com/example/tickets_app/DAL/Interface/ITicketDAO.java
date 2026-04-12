package com.example.tickets_app.DAL.Interface;

import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.BLL.util.ExceptionHandler;

import java.util.List;

public interface ITicketDAO {
    List<Ticket> getAllTickets() throws ExceptionHandler;
    void createTicket(Ticket ticket) throws ExceptionHandler;
    void deleteTicket(int ticketId) throws ExceptionHandler;
    void editTicket(int ticketId, Integer eventId, double price, double discount,
                    String ticketType, boolean isGlobal) throws ExceptionHandler;
    List<Ticket> getTicketsByEventId(int eventId) throws ExceptionHandler;
}