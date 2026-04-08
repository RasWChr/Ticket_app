package com.example.tickets_app.GUI.Controller.Ticket;

import com.example.tickets_app.BE.Event;
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
    @FXML private Label lblPrice;
    @FXML private Label lblUuid;

    public void setTicket(Ticket ticket, Event event) {
        lblEventTitle.setText(event != null ? event.getName() : ticket.getEventName());
        lblTicketType.setText(ticket.getTicketType().toUpperCase());

        if (event != null) {
            lblDate.setText(event.getStartDateTime()
                    + (event.getEndDateTime() != null && !event.getEndDateTime().isBlank()
                    ? "  →  " + event.getEndDateTime() : ""));
            lblLocation.setText(event.getLocation());
            lblLocationGuidance.setText(
                    event.getLocationGuidance() != null && !event.getLocationGuidance().isBlank()
                            ? event.getLocationGuidance() : "");
        } else {
            lblDate.setText("—");
            lblLocation.setText("—");
            lblLocationGuidance.setText("");
        }

        double price    = ticket.getPrice();
        double discount = ticket.getDiscount();
        if (discount > 0) {
            double finalPrice = price - (price * (discount / 100));
            lblPrice.setText(String.format("%.2f kr  (%.0f%% off)", finalPrice, discount));
        } else {
            lblPrice.setText(price == 0 ? "FREE" : String.format("%.2f kr", price));
        }

        lblUuid.setText("ID: " + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    }

    public void setTicket(Ticket ticket) {
        setTicket(ticket, null);
    }
}