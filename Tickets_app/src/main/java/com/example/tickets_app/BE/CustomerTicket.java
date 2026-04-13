package com.example.tickets_app.BE;

import java.time.LocalDateTime;

/**
 * A ticket issued to a specific customer.
 * Each instance has its own TicketUUID for QR/barcode — unique regardless of customer.
 * isGlobal = true → ticket template was valid for all events.
 * isUsed   = true → one-time-use ticket has already been redeemed.
 */
public class CustomerTicket {

    private int id;
    private int ticketId;
    private Integer eventId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDateTime issuedAt;
    private boolean isGlobal;
    private String ticketUUID;      // unique per issued ticket (QR/barcode content)
    private boolean isUsed;         // one-time-use flag

    private String ticketType;
    private double price;
    private double discount;
    private String eventName;

    /** Full constructor (from DB). */
    public CustomerTicket(int id, int ticketId, Integer eventId,
                          String firstName, String lastName,
                          String email, String phone,
                          LocalDateTime issuedAt, boolean isGlobal) {
        this.id        = id;
        this.ticketId  = ticketId;
        this.eventId   = eventId;
        this.firstName = firstName;
        this.lastName  = lastName;
        this.email     = email;
        this.phone     = phone;
        this.issuedAt  = issuedAt;
        this.isGlobal  = isGlobal;
    }

    /** Create constructor (before DB insert). UUID is assigned externally. */
    public CustomerTicket(int ticketId, Integer eventId,
                          String firstName, String lastName,
                          String email, String phone,
                          boolean isGlobal, String ticketUUID) {
        this.ticketId   = ticketId;
        this.eventId    = eventId;
        this.firstName  = firstName;
        this.lastName   = lastName;
        this.email      = email;
        this.phone      = phone;
        this.isGlobal   = isGlobal;
        this.ticketUUID = ticketUUID;
    }

    // ── Getters / Setters ─────────────────────────────────────────────────────

    public int getId()                          { return id; }
    public void setId(int id)                   { this.id = id; }

    public int getTicketId()                    { return ticketId; }
    public void setTicketId(int ticketId)       { this.ticketId = ticketId; }

    public Integer getEventId()                 { return eventId; }
    public void setEventId(Integer eventId)     { this.eventId = eventId; }

    public String getFirstName()                { return firstName; }
    public void setFirstName(String v)          { this.firstName = v; }

    public String getLastName()                 { return lastName; }
    public void setLastName(String v)           { this.lastName = v; }

    public String getEmail()                    { return email; }
    public void setEmail(String v)              { this.email = v; }

    public String getPhone()                    { return phone; }
    public void setPhone(String v)              { this.phone = v; }

    public LocalDateTime getIssuedAt()          { return issuedAt; }
    public void setIssuedAt(LocalDateTime v)    { this.issuedAt = v; }

    public boolean isGlobal()                   { return isGlobal; }
    public void setGlobal(boolean v)            { this.isGlobal = v; }

    public String getTicketUUID()               { return ticketUUID; }
    public void setTicketUUID(String v)         { this.ticketUUID = v; }

    public boolean isUsed()                     { return isUsed; }
    public void setUsed(boolean v)              { this.isUsed = v; }

    // Joined display fields
    public String getTicketType()               { return ticketType; }
    public void setTicketType(String v)         { this.ticketType = v; }

    public double getPrice()                    { return price; }
    public void setPrice(double v)              { this.price = v; }

    public double getDiscount()                 { return discount; }
    public void setDiscount(double v)           { this.discount = v; }

    public String getEventName()                { return eventName; }
    public void setEventName(String v)          { this.eventName = v; }

    public String getFullName()  { return firstName + " " + lastName; }

    public String getScopeLabel() {
        return isGlobal ? "All Events"
                : (eventName != null ? eventName : (eventId != null ? "Event #" + eventId : "—"));
    }

    @Override
    public String toString() { return getFullName() + " — " + getScopeLabel(); }
}