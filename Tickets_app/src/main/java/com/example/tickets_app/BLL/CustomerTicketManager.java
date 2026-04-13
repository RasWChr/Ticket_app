package com.example.tickets_app.BLL;

import com.example.tickets_app.BE.CustomerTicket;
import com.example.tickets_app.BLL.Interface.ICustomerTicketManager;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.BLL.util.ValidationUtil;
import com.example.tickets_app.DAL.Interface.ICustomerTicketDAO;

import java.util.ArrayList;
import java.util.List;

public class CustomerTicketManager implements ICustomerTicketManager {

    private final ICustomerTicketDAO dao;

    public CustomerTicketManager(ICustomerTicketDAO dao) {
        this.dao = dao;
    }

    private void validateInputs(String firstName, String lastName,
                                String email, String phone,
                                boolean isGlobal, Integer eventId) {
        if (firstName == null || firstName.isBlank())
            throw new IllegalArgumentException("First name cannot be empty.");
        if (lastName == null || lastName.isBlank())
            throw new IllegalArgumentException("Last name cannot be empty.");
        if (!ValidationUtil.isValidEmail(email))
            throw new IllegalArgumentException("Please enter a valid email address.");
        if (phone != null && !phone.isBlank() && !ValidationUtil.isValidPhone(phone))
            throw new IllegalArgumentException("Please enter a valid phone number.");
        if (!isGlobal && eventId == null)
            throw new IllegalArgumentException(
                    "Please select an event, or mark the ticket as valid for all events.");
    }

    @Override
    public int issueTicket(int ticketId, Integer eventId,
                           String firstName, String lastName,
                           String email, String phone,
                           boolean isGlobal) throws ExceptionHandler {
        validateInputs(firstName, lastName, email, phone, isGlobal, eventId);
        try {
            String uuid = dao.generateUniqueUUID();
            CustomerTicket ct = new CustomerTicket(
                    ticketId,
                    isGlobal ? null : eventId,
                    firstName.trim(), lastName.trim(),
                    email.trim(),
                    phone != null ? phone.trim() : null,
                    isGlobal,
                    uuid
            );
            return dao.issueTicket(ct);
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not issue ticket: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Integer> issueMultipleTickets(int ticketId, Integer eventId, String firstName, String lastName, String email, String phone, boolean isGlobal, int quantity) throws ExceptionHandler {
        if (quantity < 1)
            throw new IllegalArgumentException("Quantity must be at least 1.");
        if (quantity > 100)
            throw new IllegalArgumentException("Cannot issue more than 100 tickets at once.");

        validateInputs(firstName, lastName, email, phone, isGlobal, eventId);

        List<Integer> ids = new ArrayList<>();
        try {
            for (int i = 0; i < quantity; i++) {
                // Each ticket gets its own UUID — unique regardless of customer or quantity
                String uuid = dao.generateUniqueUUID();
                CustomerTicket ct = new CustomerTicket(
                        ticketId,
                        isGlobal ? null : eventId,
                        firstName.trim(), lastName.trim(),
                        email.trim(),
                        phone != null ? phone.trim() : null,
                        isGlobal,
                        uuid
                );
                ids.add(dao.issueTicket(ct));
            }
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not issue tickets: " + e.getMessage(), e);
        }
        return ids;
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
    public List<CustomerTicket> getIssuedTicketsByEmail(String email) throws ExceptionHandler {
        if (email == null || email.isBlank())
            throw new IllegalArgumentException("Email address cannot be empty.");
        try { return dao.getIssuedTicketsByEmail(email);
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not retrieve tickets by email: " + e.getMessage(), e);
        }
    }

    @Override
    public CustomerTicket validateAndUse(String uuid) throws ExceptionHandler {
        if (uuid == null || uuid.isBlank())
            throw new IllegalArgumentException("UUID cannot be empty.");

        try {
            CustomerTicket ct = dao.getByUUID(uuid);
            if (ct == null)
                return null; // not found

            if (ct.isUsed())
                throw new IllegalStateException("Ticket has already been used.");

            boolean marked = dao.markAsUsed(uuid);
            if (!marked)
                // This should never/rarely happen because we check isUsed() first — but if it does, it means the ticket was just marked used by another process after we checked.
                throw new IllegalStateException("Ticket has already been used.");

            ct.setUsed(true);
            return ct;
        } catch (ExceptionHandler e) {
            throw new ExceptionHandler("Could not validate ticket: " + e.getMessage(), e);
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