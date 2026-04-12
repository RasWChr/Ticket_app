package com.example.tickets_app.GUI.Controller.Ticket;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.BLL.Interface.ITicketManager;
import com.example.tickets_app.BLL.TicketManager;
import com.example.tickets_app.DAL.DAO.EventDAO;
import com.example.tickets_app.DAL.DAO.TicketDAO;
import com.example.tickets_app.GUI.Controller.Misc.MenuBarController;
import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import com.example.tickets_app.GUI.util.SessionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.util.List;

public class TicketCreatorController {

    @FXML private TextField  txtPrice;
    @FXML private TextField  txtDiscount;
    @FXML private TextField  txtType;
    @FXML private ChoiceBox<Event> cBoxEvent;
    @FXML private CheckBox   chkGlobal;
    @FXML private Label      lblEventScope;
    @FXML private Label      lblError;

    @FXML private MenuBarController menuBarController;
    @FXML private StackPane rootStack;

    private static final ITicketManager ticketManager =
            new TicketManager(new EventDAO(), new TicketDAO());
    private Ticket ticketToEdit = null;
    
    @FXML
    public void initialize() {
        if (!SessionManager.isLoggedIn()) {
            Platform.runLater(SessionManager::redirectToLogin);
            return;
        }

        menuBarController.setup(rootStack, "Create Ticket");

        List<Event> events = ticketManager.getAllEvents();
        cBoxEvent.setItems(FXCollections.observableArrayList(events));

        // Checkbox toggles the event picker
        chkGlobal.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            cBoxEvent.setDisable(isSelected);
            if (isSelected) {
                lblEventScope.setText("This ticket will be valid at any event.");
                cBoxEvent.setValue(null);
            } else {
                lblEventScope.setText("Or select a specific event below.");
            }
        });
    }

    public void setTicketToEdit(Ticket ticket) {
        this.ticketToEdit = ticket;

        if (cBoxEvent.getItems().isEmpty()) {
            cBoxEvent.setItems(FXCollections.observableArrayList(ticketManager.getAllEvents()));
        }

        // Restore global state first so the listener fires before we set the event value
        chkGlobal.setSelected(ticket.isGlobal());

        if (!ticket.isGlobal() && ticket.getEventID() != null) {
            cBoxEvent.getItems().stream()
                    .filter(e -> e.getId() == ticket.getEventID())
                    .findFirst()
                    .ifPresent(cBoxEvent::setValue);
        }

        txtType.setText(ticket.getTicketType());
        txtPrice.setText(String.valueOf(ticket.getPrice()));
        txtDiscount.setText(String.valueOf(ticket.getDiscount()));
    }

    public void setTicket(Ticket ticket) { setTicketToEdit(ticket); }


    @FXML
    public void onCreateTicketClick(ActionEvent actionEvent) {
        clearError();

        boolean isGlobal = chkGlobal.isSelected();
        Event selectedEvent = cBoxEvent.getValue();
        Integer eventId = (selectedEvent != null) ? selectedEvent.getId() : null;
        String ticketType = txtType.getText().trim();

        double price, discount;
        try {
            price = Double.parseDouble(txtPrice.getText().trim());
        } catch (NumberFormatException e) {
            showError("Price must be a number.");
            return;
        }
        try {
            discount = Double.parseDouble(txtDiscount.getText().trim());
        } catch (NumberFormatException e) {
            showError("Discount must be a number (0–100).");
            return;
        }

        try {
            if (ticketToEdit == null) {
                ticketManager.createTicket(eventId, price, discount, ticketType, isGlobal);
                String scope = isGlobal ? "all events"
                        : (selectedEvent != null ? selectedEvent.getName() : "—");
                AlertUtil.showInfo("Ticket saved",
                        "Ticket \"" + ticketType + "\" created for " + scope + ".");
            } else {
                ticketManager.editTicket(ticketToEdit.getId(), eventId,
                        price, discount, ticketType, isGlobal);
                AlertUtil.showInfo("Ticket updated",
                        "Ticket \"" + ticketType + "\" has been updated.");
            }
            SceneUtil.switchScene(actionEvent, "Views/Ticket/TicketList.fxml");

        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            AlertUtil.showError("Database error", e.getMessage());
        }
    }

    @FXML
    public void onBtnCancelClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Ticket/TicketList.fxml");
    }

    private void showError(String msg) {
        lblError.setText(msg);
        lblError.setVisible(true);
        lblError.setManaged(true);
    }

    private void clearError() {
        lblError.setVisible(false);
        lblError.setManaged(false);
        lblError.setText("");
    }
}