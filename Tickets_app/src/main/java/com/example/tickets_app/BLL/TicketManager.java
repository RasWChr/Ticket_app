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
        this.eventDAO  = eventDAO;
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
    public List<Ticket> getTicketsByEventId(int eventId) throws ExceptionHandler {
        return ticketDAO.getTicketsByEventId(eventId);
    }

    @Override
    public void createTicket(Integer eventId, double price, double discount,
                             String ticketType, boolean isGlobal) throws ExceptionHandler {
        if (!isGlobal && eventId == null)
            throw new IllegalArgumentException("Must choose an event, or mark as valid for all events.");
        if (!ValidationUtil.isValidTicketType(ticketType))
            throw new IllegalArgumentException("Must be a valid ticket type.");
        if (!ValidationUtil.isValidPrice(price))
            throw new IllegalArgumentException("Must be a valid price.");
        if (!ValidationUtil.isValidDiscount(discount))
            throw new IllegalArgumentException("Must be a valid discount.");

        try {
            Ticket ticket = new Ticket(isGlobal ? null : eventId, price, discount, ticketType, isGlobal);
            ticketDAO.createTicket(ticket);
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not create ticket: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteTicket(int ticketId) throws ExceptionHandler {
        try {
            ticketDAO.deleteTicket(ticketId);
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not delete ticket: " + e.getMessage(), e);
        }
    }

    @Override
    public void editTicket(int id, Integer eventId, double price, double discount,
                           String ticketType, boolean isGlobal) throws ExceptionHandler {
        if (!isGlobal && eventId == null)
            throw new IllegalArgumentException("Must choose an event, or mark as valid for all events.");
        if (!ValidationUtil.isValidTicketType(ticketType))
            throw new IllegalArgumentException("Must be a valid ticket type.");
        if (!ValidationUtil.isValidPrice(price))
            throw new IllegalArgumentException("Must be a valid price.");
        if (!ValidationUtil.isValidDiscount(discount))
            throw new IllegalArgumentException("Must be a valid discount.");

        try {
            ticketDAO.editTicket(id, isGlobal ? null : eventId, price, discount, ticketType, isGlobal);
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not edit ticket: " + e.getMessage(), e);
        }
    }
}