package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.BLL.Interface.ITicketManager;
import com.example.tickets_app.BLL.TicketManager;
import com.example.tickets_app.DAL.DAO.EventDAO;
import com.example.tickets_app.DAL.DAO.TicketDAO;
import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.List;

public class TicketCreatorController {
    @FXML private TextField txtPrice;
    @FXML private ChoiceBox cBoxEvent;
    @FXML private TextField txtDiscount;
    @FXML private TextField txtType;

    private final static ITicketManager ticketManager = new TicketManager(new EventDAO(), new TicketDAO());
    private Ticket ticketToEdit = null;

    @FXML
    public void initialize() {
        List<Event> events = ticketManager.getAllEvents();
        ObservableList<Event> eventList = FXCollections.observableArrayList(events);
        cBoxEvent.setItems(eventList);
    }

    public void setTicketToEdit(Ticket ticket) {
        this.ticketToEdit = ticket;

        if (cBoxEvent.getItems().isEmpty()) {
            List<Event> events = ticketManager.getAllEvents();
            cBoxEvent.setItems(FXCollections.observableArrayList(events));
        }

        cBoxEvent.getItems().stream()
                .filter(e -> ((Event) e).getId() == ticket.getEventID())
                .findFirst()
                .ifPresent(cBoxEvent::setValue);

        txtType.setText(ticket.getTicketType());
        txtPrice.setText(String.valueOf(ticket.getPrice()));
        txtDiscount.setText(String.valueOf(ticket.getDiscount()));
    }

    public void setTicket(Ticket ticket) {
        setTicketToEdit(ticket);
    }

    public void onCreateTicketClick(ActionEvent actionEvent) {
        Event selectedEvent       = cBoxEvent.getValue() != null ? (Event) cBoxEvent.getValue() : null;
        int eventId               = cBoxEvent.getValue() != null ? ((Event) cBoxEvent.getValue()).getId() : 0;
        String ticketType         = txtType         != null ? txtType.getText()         : "";
        String eventName          = selectedEvent != null ? selectedEvent.getName() : "Unknown Event";
        double price              = Double.parseDouble(txtPrice != null ? txtPrice.getText() : "");
        double discount           = Double.parseDouble(txtDiscount      != null ? txtDiscount.getText()      : "");


        try {
            if (ticketToEdit == null) {
                ticketManager.createTicket(eventId, price, discount, ticketType);
                AlertUtil.showInfo("Ticket saved", "Ticket \"" + eventName + " " + ticketType + "\" has been created.");
            } else {
                ticketManager.editTicket(ticketToEdit.getId(), eventId, price, discount, ticketType);
                AlertUtil.showInfo("Ticket updated", "Ticket \"" + eventName + " " + ticketType + "\" has been updated.");
            }
            SceneUtil.switchScene(actionEvent, "Views/TicketList.fxml");
        } catch (IllegalArgumentException e) {
            AlertUtil.showWarning("Invalid input", e.getMessage());
        } catch (Exception e) {
            AlertUtil.showError("Database error", e.getMessage());
        }
    }

    public void onBtnCancelClick(ActionEvent actionEvent) { SceneUtil.switchScene(actionEvent, "Views/TicketList.fxml");
    }
}
