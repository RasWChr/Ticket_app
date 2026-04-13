package com.example.tickets_app.DAL.Interface;

import com.example.tickets_app.BE.CustomerTicket;
import com.example.tickets_app.BLL.util.ExceptionHandler;

import java.util.List;

public interface ICustomerTicketDAO {

    /** Issue a ticket to a customer. Returns the generated DB ID. */
    int issueTicket(CustomerTicket customerTicket) throws ExceptionHandler;

    List<CustomerTicket> getAllIssuedTickets() throws ExceptionHandler;

    List<CustomerTicket> getIssuedTicketsForEvent(int eventId) throws ExceptionHandler;

    List<CustomerTicket> getIssuedTicketsByTicketId(int ticketId) throws ExceptionHandler;

    /** All issued tickets for a given email — supports multiple tickets per customer. */
    List<CustomerTicket> getIssuedTicketsByEmail(String email) throws ExceptionHandler;

    /** Lookup by the issued ticket's unique UUID (for scan/validate). */
    CustomerTicket getByUUID(String uuid) throws ExceptionHandler;

    /**
     * Atomically marks the ticket as used if it hasn't been used yet.
     * @return true if successfully marked used; false if already used.
     */
    boolean markAsUsed(String uuid) throws ExceptionHandler;

    void deleteIssuedTicket(int customerTicketId) throws ExceptionHandler;

    /** Generates a UUID guaranteed unique in the CustomerTickets table. */
    String generateUniqueUUID() throws ExceptionHandler;
}