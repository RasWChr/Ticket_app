package com.example.tickets_app.BLL;

import com.example.tickets_app.BE.CustomerTicket;
import com.example.tickets_app.BLL.Interface.ICustomerTicketManager;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.BLL.util.ValidationUtil;
import com.example.tickets_app.DAL.Interface.ICustomerTicketDAO;

import java.util.List;

public class CustomerTicketManager implements ICustomerTicketManager {

    private final ICustomerTicketDAO dao;

    public CustomerTicketManager(ICustomerTicketDAO dao) {
        this.dao = dao;
    }

    @Override
    public int issueTicket(int ticketId, Integer eventId,
                           String firstName, String lastName,
                           String email, String phone,
                           boolean isGlobal) throws ExceptionHandler {

        // Validation
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be empty.");
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be empty.");
        if (!ValidationUtil.isValidEmail(email))
            throw new IllegalArgumentException("Please enter a valid email address.");
        if (phone != null && !phone.isBlank() && !ValidationUtil.isValidPhone(phone))
            throw new IllegalArgumentException("Please enter a valid phone number.");

        // If not global, an event must be selected
        if (!isGlobal && eventId == null)
            throw new IllegalArgumentException("Please select an event, or mark the ticket as valid for all events.");

        try {
            CustomerTicket ct = new CustomerTicket(
                    ticketId,
                    isGlobal ? null : eventId,
                    firstName.trim(),
                    lastName.trim(),
                    email.trim(),
                    phone != null ? phone.trim() : null,
                    isGlobal
            );
            return dao.issueTicket(ct);
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not issue ticket: " + e.getMessage(), e);
        }
    }

    @Override
    public List<CustomerTicket> getAllIssuedTickets() throws ExceptionHandler {
        try {
            return dao.getAllIssuedTickets();
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not retrieve issued tickets: " + e.getMessage(), e);
        }
    }

    @Override
    public List<CustomerTicket> getIssuedTicketsForEvent(int eventId) throws ExceptionHandler {
        try {
            return dao.getIssuedTicketsForEvent(eventId);
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not retrieve tickets for event: " + e.getMessage(), e);
        }
    }

    @Override
    public List<CustomerTicket> getIssuedTicketsByTicketId(int ticketId) throws ExceptionHandler {
        try {
            return dao.getIssuedTicketsByTicketId(ticketId);
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not retrieve issued tickets: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteIssuedTicket(int customerTicketId) throws ExceptionHandler {
        try {
            dao.deleteIssuedTicket(customerTicketId);
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not delete issued ticket: " + e.getMessage(), e);
        }
    }
}