package com.example.tickets_app.GUI.Controller.Ticket;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.BLL.Interface.ITicketManager;
import com.example.tickets_app.BLL.TicketManager;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.DAO.EventDAO;
import com.example.tickets_app.DAL.DAO.TicketDAO;
import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import java.util.List;


public class TicketController {

    @FXML private ChoiceBox<Ticket> cBoxTickets;
    @FXML private ChoiceBox<Event> cBoxEvent;
    @FXML private TextField txtFirstNT;
    @FXML private TextField txtLastNT;
    @FXML private TextField txtEmailT;
    @FXML private TextField txtPhoneT;

    private final ITicketManager ticketManager = new TicketManager(new EventDAO(), new TicketDAO());

    @FXML
    public void initialize() {
        try {
            List<Event> events = ticketManager.getAllEvents();
            cBoxEvent.setItems(FXCollections.observableArrayList(events));
        } catch (Exception e) {
            AlertUtil.showError("Database error", "Could not load events: " + e.getMessage());
        }

        // When an event is selected, load its tickets
        cBoxEvent.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    List<Ticket> tickets = ticketManager.getTicketsByEventId(newVal.getId());
                    cBoxTickets.setItems(FXCollections.observableArrayList(tickets));
                } catch (ExceptionHandler e) {
                    AlertUtil.showError("Database error", "Could not load tickets: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    public void onBtnCancelClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
    }

    @FXML
    public void onPTicketClick(ActionEvent actionEvent) {
        String firstName = txtFirstNT != null ? txtFirstNT.getText() : "";
        String lastName = txtLastNT != null ? txtLastNT.getText() : "";
        String email = txtEmailT != null ? txtEmailT.getText() : "";

        if (firstName.isBlank() || lastName.isBlank() || email.isBlank()) {
            AlertUtil.showWarning("Missing information", "Please fill in first name, last name and email before printing the ticket.");
            return;
        }

        Ticket selectedTicket = cBoxTickets.getValue();
        Event selectedEvent = cBoxEvent.getValue();

        if (selectedTicket == null) {
            AlertUtil.showWarning("Missing information", "Please select a ticket.");
            return;
        }

        TicketPreviewController.openAsWindow(selectedTicket, selectedEvent, firstName + " " + lastName, email);
    }
}