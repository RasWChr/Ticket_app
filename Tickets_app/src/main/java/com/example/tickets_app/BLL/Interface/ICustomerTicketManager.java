package com.example.tickets_app.BLL.Interface;

import com.example.tickets_app.BE.CustomerTicket;
import com.example.tickets_app.BLL.util.ExceptionHandler;

import java.util.List;

public interface ICustomerTicketManager {

    /**
     * Issues ONE ticket to a customer. Returns the DB-generated ID.
     * Each call produces a unique TicketUUID — so calling this N times for
     * the same email creates N independent tickets, each with their own QR.
     */
    int issueTicket(int ticketId, Integer eventId,
                    String firstName, String lastName,
                    String email, String phone,
                    boolean isGlobal) throws ExceptionHandler;

    /**
     * Issues MULTIPLE tickets at once (bulk / group purchase).
     * @param quantity  number of tickets to issue (must be ≥ 1)
     * @return list of generated CustomerTicket IDs
     */
    List<Integer> issueMultipleTickets(int ticketId, Integer eventId,
                                       String firstName, String lastName,
                                       String email, String phone,
                                       boolean isGlobal,
                                       int quantity) throws ExceptionHandler;

    List<CustomerTicket> getAllIssuedTickets() throws ExceptionHandler;
    List<CustomerTicket> getIssuedTicketsForEvent(int eventId) throws ExceptionHandler;
    List<CustomerTicket> getIssuedTicketsByTicketId(int ticketId) throws ExceptionHandler;
    List<CustomerTicket> getIssuedTicketsByEmail(String email) throws ExceptionHandler;

    /**
     * Validates a scanned UUID.
     * @return the CustomerTicket if valid and unused; null if not found.
     * @throws IllegalStateException if the ticket has already been used.
     */
    CustomerTicket validateAndUse(String uuid) throws ExceptionHandler;

    void deleteIssuedTicket(int customerTicketId) throws ExceptionHandler;
}