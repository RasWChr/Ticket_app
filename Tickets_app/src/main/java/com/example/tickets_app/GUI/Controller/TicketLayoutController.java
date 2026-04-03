package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.Ticket;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.UUID;

public class TicketLayoutController {

    @FXML private Label lblEventTitle;
    @FXML private Label lblTicketType;
    @FXML private Label lblDate;
    @FXML private Label lblLocation;
    @FXML private Label lblLocationGuidance;
    @FXML private Label lblCustomerName;
    @FXML private Label lblCustomerEmail;
    @FXML private Label lblPrice;
    @FXML private Label lblUuid;

    public void setTicket(Ticket ticket) {
        lblEventTitle.setText(ticket.getEventName());
        lblTicketType.setText(ticket.getTicketType().toUpperCase());

        // Date and location come from the event — leave blank for now
        // as Ticket doesn't carry event details beyond name
        lblDate.setText("—");
        lblLocation.setText("—");
        lblLocationGuidance.setText("");
        lblCustomerName.setText("—");
        lblCustomerEmail.setText("—");

        // Calculate discounted price
        double price = ticket.getPrice();
        double discount = ticket.getDiscount();
        if (discount > 0) {
            double finalPrice = price - (price * discount);
            lblPrice.setText(String.format("%.2f kr", finalPrice));
        } else {
            lblPrice.setText(String.format("%.2f kr", price));
        }

        lblUuid.setText("ID: " + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    }
}