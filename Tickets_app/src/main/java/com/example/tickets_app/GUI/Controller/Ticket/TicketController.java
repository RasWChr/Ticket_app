package com.example.tickets_app.GUI.Controller.Ticket;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.BLL.Interface.ITicketManager;
import com.example.tickets_app.BLL.TicketManager;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.DAO.EventDAO;
import com.example.tickets_app.DAL.DAO.TicketDAO;
import com.example.tickets_app.GUI.Controller.Misc.MenuBarController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.util.List;

public class TicketController {

    @FXML private ChoiceBox<Ticket> cBoxTickets;
    @FXML private ChoiceBox<Event> cBoxEvent;
    @FXML private TextField txtFirstNT;
    @FXML private TextField txtLastNT;
    @FXML private TextField txtEmailT;
    @FXML private TextField txtPhoneT;
    @FXML private Label lblError;

    private final ITicketManager ticketManager =
            new TicketManager(new EventDAO(), new TicketDAO());

    @FXML private MenuBarController menuBarController;
    @FXML private StackPane rootStack;

    @FXML
    public void initialize() {
        try {
            List<Event> events = ticketManager.getAllEvents();
            cBoxEvent.setItems(FXCollections.observableArrayList(events));
        } catch (Exception e) {
            showError("Could not load events: " + e.getMessage());
        }

        menuBarController.setup(rootStack, "Ticket List");

        cBoxEvent.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                try {
                    List<Ticket> tickets = ticketManager.getTicketsByEventId(newVal.getId());
                    cBoxTickets.setItems(FXCollections.observableArrayList(tickets));
                } catch (ExceptionHandler e) {
                    showError("Could not load tickets: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    public void onPTicketClick(ActionEvent actionEvent) {
        clearErrors();

        String firstName = txtFirstNT.getText().trim();
        String lastName  = txtLastNT.getText().trim();
        String email     = txtEmailT.getText().trim();

        boolean hasError = false;

        if (firstName.isBlank()) {
            setFieldError(txtFirstNT, true);
            hasError = true;
        }

        if (lastName.isBlank()) {
            setFieldError(txtLastNT, true);
            hasError = true;
        }

        if (email.isBlank()) {
            setFieldError(txtEmailT, true);
            hasError = true;
        } else {
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
            if (!email.matches(emailRegex)) {
                setFieldError(txtEmailT, true);
                showError("Please enter a valid email address.");
                return;
            }
        }

        if (hasError) {
            showError("Please fill in all required fields.");
            return;
        }

        Ticket selectedTicket = cBoxTickets.getValue();
        Event selectedEvent   = cBoxEvent.getValue();

        if (selectedTicket == null) {
            showError("Please select a ticket.");
            return;
        }

        TicketPreviewController.openAsWindow(
                selectedTicket,
                selectedEvent,
                firstName + " " + lastName,
                email
        );
    }

    private void showError(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
        lblError.setManaged(true);
    }

    private void clearErrors() {
        lblError.setVisible(false);
        lblError.setManaged(false);
        lblError.setText("");

        setFieldError(txtFirstNT, false);
        setFieldError(txtLastNT, false);
        setFieldError(txtEmailT, false);
        setFieldError(txtPhoneT, false);
    }

    private void setFieldError(javafx.scene.control.Control field, boolean error) {
        if (field == null) return;

        if (error) {
            if (!field.getStyleClass().contains("field-error")) {
                field.getStyleClass().add("field-error");
            }
        } else {
            field.getStyleClass().remove("field-error");
        }
    }
}
