package com.example.tickets_app.BLL.Interface;

import com.example.tickets_app.BE.CustomerTicket;
import com.example.tickets_app.BLL.util.ExceptionHandler;

import java.util.List;

public interface ICustomerTicketManager {

    /**
     * Issue a ticket to a customer.
     *
     * @param ticketId  The base ticket template ID
     * @param eventId   The event to lock to, or null for a global ticket
     * @param firstName Customer first name
     * @param lastName  Customer last name
     * @param email     Customer email (required)
     * @param phone     Customer phone (optional)
     * @param isGlobal  true = valid for all events, false = locked to eventId
     * @return the new CustomerTicket record ID
     */
    int issueTicket(int ticketId, Integer eventId,
                    String firstName, String lastName,
                    String email, String phone,
                    boolean isGlobal) throws ExceptionHandler;

    List<CustomerTicket> getAllIssuedTickets() throws ExceptionHandler;

    List<CustomerTicket> getIssuedTicketsForEvent(int eventId) throws ExceptionHandler;

    List<CustomerTicket> getIssuedTicketsByTicketId(int ticketId) throws ExceptionHandler;

    void deleteIssuedTicket(int customerTicketId) throws ExceptionHandler;
}