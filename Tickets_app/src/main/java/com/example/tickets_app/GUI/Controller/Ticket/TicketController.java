package com.example.tickets_app.GUI.Controller.Ticket;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.BLL.CustomerTicketManager;
import com.example.tickets_app.BLL.Interface.ICustomerTicketManager;
import com.example.tickets_app.BLL.Interface.ITicketManager;
import com.example.tickets_app.BLL.TicketManager;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.DAO.CustomerTicketDAO;
import com.example.tickets_app.DAL.DAO.EventDAO;
import com.example.tickets_app.DAL.DAO.TicketDAO;
import com.example.tickets_app.GUI.Controller.Misc.MenuBarController;
import com.example.tickets_app.GUI.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.util.List;

public class TicketController {

    @FXML private ChoiceBox<Ticket> cBoxTickets;
    @FXML private ChoiceBox<Event>  cBoxEvent;
    @FXML private TextField  txtFirstNT;
    @FXML private TextField  txtLastNT;
    @FXML private TextField  txtEmailT;
    @FXML private TextField  txtPhoneT;
    @FXML private Label      lblError;

    @FXML private MenuBarController menuBarController;
    @FXML private StackPane rootStack;

    private final ITicketManager ticketManager =
            new TicketManager(new EventDAO(), new TicketDAO());
    private final ICustomerTicketManager customerTicketManager =
            new CustomerTicketManager(new CustomerTicketDAO());

    @FXML
    public void initialize() {
        menuBarController.setup(rootStack, "Issue Ticket");

        try {
            List<Event> events = ticketManager.getAllEvents();
            cBoxEvent.setItems(FXCollections.observableArrayList(events));
        } catch (Exception e) {
            showError("Could not load events: " + e.getMessage());
        }

        // When event selected → load its tickets (includes global ones)
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
        String phone     = txtPhoneT.getText().trim();

        boolean hasError = false;
        if (firstName.isBlank()) { setFieldError(txtFirstNT, true); hasError = true; }
        if (lastName.isBlank())  { setFieldError(txtLastNT,  true); hasError = true; }
        if (email.isBlank()) {
            setFieldError(txtEmailT, true); hasError = true;
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            setFieldError(txtEmailT, true);
            showError("Please enter a valid email address.");
            return;
        }
        if (hasError) { showError("Please fill in all required fields."); return; }

        Ticket selectedTicket = cBoxTickets.getValue();
        Event  selectedEvent  = cBoxEvent.getValue();

        if (selectedEvent == null) { showError("Please select an event."); return; }
        if (selectedTicket == null) { showError("Please select a ticket type."); return; }

        // Determine effective event scope:
        // If the ticket template is global, the CustomerTicket is still linked to the
        // event the coordinator is issuing it FOR (so we know which event it was used at),
        // but IsGlobal stays true in the template.
        try {
            customerTicketManager.issueTicket(
                    selectedTicket.getId(),
                    selectedEvent.getId(),   // always record which event it was issued at
                    firstName, lastName,
                    email,
                    phone.isBlank() ? null : phone,
                    selectedTicket.isGlobal() // carry the template's global flag
            );
        } catch (ExceptionHandler e) {
            AlertUtil.showError("Database error", "Could not save ticket record: " + e.getMessage());
            // Don't block preview even if save failed — show it anyway
        }

        // Open preview — if template is global, don't show a specific event in the header
        Event previewEvent = selectedTicket.isGlobal() ? null : selectedEvent;
        TicketPreviewController.openAsWindow(
                selectedTicket,
                previewEvent,
                firstName + " " + lastName,
                email,
                selectedTicket.isGlobal()
        );
    }

    @FXML
    public void onBtnCancelClick(ActionEvent actionEvent) {
        javafx.scene.Node source = (javafx.scene.Node) actionEvent.getSource();
        com.example.tickets_app.GUI.util.SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
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
        setFieldError(txtLastNT,  false);
        setFieldError(txtEmailT,  false);
        setFieldError(txtPhoneT,  false);
    }

    private void setFieldError(Control field, boolean error) {
        if (field == null) return;
        if (error) { if (!field.getStyleClass().contains("field-error")) field.getStyleClass().add("field-error"); }
        else { field.getStyleClass().remove("field-error"); }
    }
}