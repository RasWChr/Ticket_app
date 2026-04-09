package com.example.tickets_app.BLL.Interface;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.BLL.util.ExceptionHandler;

import java.util.List;

public interface ITicketManager {
    List<Event> getAllEvents();
    List<Ticket> getAllTickets() throws ExceptionHandler;
    void createTicket(int eventId, double price, double discount, String ticketType) throws ExceptionHandler;
    void deleteTicket(int ticketId) throws ExceptionHandler;
    void editTicket(int ID, int eventId, double price, double discount, String ticketType) throws ExceptionHandler;
    List<Ticket> getTicketsByEventId(int eventId) throws ExceptionHandler;

}
