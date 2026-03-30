package com.example.tickets_app.BE;

public class Ticket {
    private int id;
    private int eventID;
    private double price;
    private String ticketType;
    private double discount;

    public Ticket(int id, int eventID, double price, double discount, String ticketType){
        this.id = id;
        this.eventID = eventID;
        this.price = price;
        this.discount = discount;
        this.ticketType = ticketType;
    }

    public Ticket(int eventID, double price, double discount, String ticketType){
        this.eventID = eventID;
        this.price = price;
        this.discount = discount;
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
}
