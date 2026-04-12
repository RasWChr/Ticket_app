package com.example.tickets_app.BE;

public class Ticket {
    private int id;
    private Integer eventID;   // nullable — null means global
    private double price;
    private String ticketType;
    private double discount;
    private String eventName;
    private boolean isGlobal;

    public Ticket(int id, Integer eventID, double price, double discount,
                  String ticketType, boolean isGlobal) {
        this.id = id;
        this.eventID = eventID;
        this.price = price;
        this.discount = discount;
        this.ticketType = ticketType;
        this.isGlobal = isGlobal;
    }

    public Ticket(Integer eventID, double price, double discount,
                  String ticketType, boolean isGlobal) {
        this.eventID = eventID;
        this.price = price;
        this.discount = discount;
        this.ticketType = ticketType;
        this.isGlobal = isGlobal;
    }

    public Ticket(int id, int eventID, double price, double discount, String ticketType) {
        this(id, (Integer) eventID, price, discount, ticketType, false);
    }

    public Ticket(int eventID, double price, double discount, String ticketType) {
        this((Integer) eventID, price, discount, ticketType, false);
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setGlobal(boolean global) {
        isGlobal = global;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setEventID(Integer eventID) {
        this.eventID = eventID;
    }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getEventID() {
        return eventID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getDiscount() {return discount;}

    public void setDiscount(double discount) {this.discount = discount;}

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }

    public String getScopeLabel() {
        if (isGlobal) return "All Events";
        return eventName != null ? eventName : (eventID != null ? "Event #" + eventID : "—");
    }

    @Override
    public String toString() {
        return discount > 0
                ? ticketType + " (" + price + " kr, " + discount + "%)"
                : ticketType;
    }
}
