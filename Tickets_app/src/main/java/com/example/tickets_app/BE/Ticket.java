package com.example.tickets_app.BE;

public class Ticket {
    private int id;
    private String eventName;
    private int price;
    private String image;
    private String ticketType;

    public Ticket(int id, String eventName, int price, String image, String ticketType){
        this.id = id;
        this.eventName = eventName;
        this.price = price;
        this.image = image;
        this.ticketType = ticketType;
    }

    public Ticket(String eventName, int price, String image, String ticketType){
        this.eventName = eventName;
        this.price = price;
        this.image = image;
        this.ticketType = ticketType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTicketType() {
        return ticketType;
    }

    public void setTicketType(String ticketType) {
        this.ticketType = ticketType;
    }
}
