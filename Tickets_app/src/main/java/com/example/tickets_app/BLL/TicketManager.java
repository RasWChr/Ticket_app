package com.example.tickets_app.BLL;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.BLL.Interface.ITicketManager;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.BLL.util.ValidationUtil;
import com.example.tickets_app.DAL.Interface.IEventDAO;
import com.example.tickets_app.DAL.Interface.ITicketDAO;

import java.util.List;

public class TicketManager implements ITicketManager {

    private final IEventDAO eventDAO;
    private final ITicketDAO ticketDAO;

    public TicketManager(IEventDAO eventDAO, ITicketDAO ticketDAO) {
        this.eventDAO = eventDAO;
        this.ticketDAO = ticketDAO;
    }


    @Override
    public List<Event> getAllEvents() {
        try {
            return eventDAO.getAllEvents();
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not retrieve events: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Ticket> getAllTickets() throws ExceptionHandler {
        try {
            return ticketDAO.getAllTickets();
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not retrieve tickets: " + e.getMessage(), e);
        }
    }

    @Override
    public void createTicket(int eventId, double price, double discount, String ticketType) throws ExceptionHandler {
        if (!ValidationUtil.isValidEventId(eventId))
            throw new IllegalArgumentException("Must choose an Event.");
        if (!ValidationUtil.isValidTicketType(ticketType))
            throw new IllegalArgumentException("Must be a valid ticket type.");
        if (!ValidationUtil.isValidPrice(price))
            throw new IllegalArgumentException("Must be a valid price.");
        if (!ValidationUtil.isValidDiscount(discount))
            throw new IllegalArgumentException("Must be a valid discount.");

        try {
            Ticket ticket = new Ticket(eventId, price, discount, ticketType);
            ticketDAO.createTicket(ticket);
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not create ticket: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteTicket(int ticketId) throws ExceptionHandler {
        try {
            eventDAO.deleteEvent(ticketId);
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not delete ticket: " + e.getMessage(), e);
        }
    }

    @Override
    public void editTicket(int eventId, double price, double discount, double v, String ticketType) throws ExceptionHandler {
        if (!ValidationUtil.isValidEventId(eventId))
            throw new IllegalArgumentException("Must choose an Event.");
        if (!ValidationUtil.isValidTicketType(ticketType))
            throw new IllegalArgumentException("Must be a valid ticket type.");
        if (!ValidationUtil.isValidPrice(price))
            throw new IllegalArgumentException("Must be a valid price.");
        if (!ValidationUtil.isValidDiscount(discount))
            throw new IllegalArgumentException("Must be a valid discount.");

        try {
            Ticket ticket = new Ticket(eventId, price, discount, ticketType);
            ticketDAO.createTicket(ticket);
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not create ticket: " + e.getMessage(), e);
        }
    }
}
