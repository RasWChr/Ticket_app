package com.example.tickets_app.DAL.Interface;

import com.example.tickets_app.BE.CustomerTicket;
import com.example.tickets_app.BLL.util.ExceptionHandler;

import java.util.List;

public interface ICustomerTicketDAO {

    /** Issue a ticket to a customer. Returns the generated ID. */
    int issueTicket(CustomerTicket customerTicket) throws ExceptionHandler;

    /** All issued tickets (joined with Tickets + Events for display). */
    List<CustomerTicket> getAllIssuedTickets() throws ExceptionHandler;

    /** Issued tickets for a specific event (includes global tickets). */
    List<CustomerTicket> getIssuedTicketsForEvent(int eventId) throws ExceptionHandler;

    /** Issued tickets for a specific base ticket template. */
    List<CustomerTicket> getIssuedTicketsByTicketId(int ticketId) throws ExceptionHandler;

    /** Delete an issued customer ticket record. */
    void deleteIssuedTicket(int customerTicketId) throws ExceptionHandler;
}