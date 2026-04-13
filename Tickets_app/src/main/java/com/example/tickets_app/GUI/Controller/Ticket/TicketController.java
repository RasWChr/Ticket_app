package com.example.tickets_app.GUI.Controller.Ticket;

import com.example.tickets_app.BE.CustomerTicket;
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
    @FXML private Spinner<Integer> spinnerQty;
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

        // Spinner: 1–100
        SpinnerValueFactory<Integer> factory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        spinnerQty.setValueFactory(factory);

        try {
            cBoxEvent.setItems(FXCollections.observableArrayList(ticketManager.getAllEvents()));
        } catch (Exception e) {
            showError("Could not load events: " + e.getMessage());
        }

        cBoxEvent.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            if (newVal != null) {
                try {
                    cBoxTickets.setItems(FXCollections.observableArrayList(
                            ticketManager.getTicketsByEventId(newVal.getId())));
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
        int quantity     = spinnerQty.getValue();

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

        try {
            // Issue all tickets (each gets a unique UUID)
            List<Integer> issuedIds = customerTicketManager.issueMultipleTickets(
                    selectedTicket.getId(),
                    selectedEvent.getId(),
                    firstName, lastName,
                    email,
                    phone.isBlank() ? null : phone,
                    selectedTicket.isGlobal(),
                    quantity
            );

            if (quantity > 1) {
                // For bulk: show summary and open preview only for the first ticket
                AlertUtil.showInfo("Tickets Issued",
                        quantity + " tickets issued to " + email + ".\n" +
                                "Showing preview for ticket 1 of " + quantity + ".");
            }

            // Fetch the first issued ticket's UUID for the preview
            List<CustomerTicket> issued = customerTicketManager.getIssuedTicketsByEmail(email);
            // Get the most recently issued one (first in descending list)
            CustomerTicket firstIssued = issued.isEmpty() ? null : issued.get(0);
            String previewUUID = firstIssued != null ? firstIssued.getTicketUUID() : null;

            // Open preview — pass the issued UUID so barcode/QR uses the customer-specific UUID
            Event previewEvent = selectedTicket.isGlobal() ? null : selectedEvent;
            TicketPreviewController.openAsWindow(
                    selectedTicket,
                    previewEvent,
                    firstName + " " + lastName,
                    email,
                    selectedTicket.isGlobal(),
                    previewUUID
            );

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (ExceptionHandler e) {
            AlertUtil.showError("Database error", "Could not save ticket: " + e.getMessage());
        }
    }

    @FXML
    public void onBtnCancelClick(ActionEvent actionEvent) {
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
        else field.getStyleClass().remove("field-error");
    }
}