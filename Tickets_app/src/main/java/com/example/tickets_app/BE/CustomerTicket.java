package com.example.tickets_app.BE;

import java.time.LocalDateTime;

/**
 * Represents a ticket issued to a specific customer.
 * eventId == null  →  ticket is GLOBAL (valid for all events)
 * eventId != null  →  ticket is locked to that event
 */
public class CustomerTicket{

    private int id;
    private int ticketId;
    private Integer eventId;   // nullable — null means global
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDateTime issuedAt;
    private boolean isGlobal;
    private String ticketUUID; // unique per issued ticket (QR/barcode content)
    private boolean isUsed; // one-time-use flag


    // Joined fields (not stored — populated by DAO queries)
    private String ticketType;
    private double price;
    private double discount;
    private String eventName;   // null / "All Events" when global


    /** Full constructor (from DB) */
    public CustomerTicket(int id, int ticketId, Integer eventId,
                          String firstName, String lastName,
                          String email, String phone,
                          LocalDateTime issuedAt, boolean isGlobal) {
        this.id = id;
        this.ticketId = ticketId;
        this.eventId = eventId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.issuedAt = issuedAt;
        this.isGlobal = isGlobal;
    }

    /** Create constructor (before DB insert) */
    public CustomerTicket(int ticketId, Integer eventId,
                          String firstName, String lastName,
                          String email, String phone,
                          boolean isGlobal, String ticketUUID) {
        this.ticketId = ticketId;
        this.eventId = eventId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.isGlobal = isGlobal;
        this.ticketUUID = ticketUUID;
    }

    public String getTicketUUID() {
        return ticketUUID;
    }

    public void setTicketUUID(String ticketUUID) {
        this.ticketUUID = ticketUUID;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getFullName() { return firstName + " " + lastName; }

    public String getScopeLabel() {
        return isGlobal ? "All Events" : (eventName != null ? eventName : "Event #" + eventId);
    }

    @Override
    public String toString() {
        return getFullName() + " — " + getScopeLabel();
    }
}