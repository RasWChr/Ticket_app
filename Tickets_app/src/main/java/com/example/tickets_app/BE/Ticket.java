package com.example.tickets_app.BE;

public class Ticket {
    private int id;
    private int eventID;
    private int price;
    private String ticketType;

    public Ticket(int id, int eventID, int price, String ticketType){
        this.id = id;
        this.eventID = eventID;
        this.price = price;
        this.ticketType = ticketType;
    }

    public Ticket(int eventID, int price, String ticketType){
        this.eventID = eventID;
        this.price = price;
        this.ticketType = ticketType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }
}
