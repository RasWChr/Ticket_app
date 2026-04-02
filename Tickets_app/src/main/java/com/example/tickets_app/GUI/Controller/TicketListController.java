package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.BLL.Interface.ITicketManager;
import com.example.tickets_app.BLL.TicketManager;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.DAO.EventDAO;
import com.example.tickets_app.DAL.DAO.TicketDAO;
import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import com.example.tickets_app.GUI.util.SessionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;


public class TicketListController {

    @FXML private ListView<Ticket> listViewTickets;

    private final ITicketManager ticketManager = new TicketManager(new EventDAO(), new TicketDAO());
    private final ObservableList<Ticket> ticketList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        if (!SessionManager.isLoggedIn()) {
            Platform.runLater(SessionManager::redirectToLogin);
            return;
        }

        listViewTickets.setItems(ticketList);
        listViewTickets.setCellFactory(lv ->
                new TicketListCell(this::handleDelete, this::handleEdit));
        loadTickets();
    }

    private void loadTickets() {
        try {
            ticketList.setAll(ticketManager.getAllTickets());
        } catch (ExceptionHandler e) {
            AlertUtil.showError("Database error", "Could not load Tickets: " + e.getMessage());
        }
    }

    private void handleDelete(Ticket ticket) {
        if (AlertUtil.showConfirmation("Delete Ticket",
                "Are you sure you want to delete " + ticket.getEventID() + "?")) {
            try {
                ticketManager.deleteTicket(ticket.getId());
                ticketList.remove(ticket);
                AlertUtil.showInfo("Deleted", ticket.getEventID() + " has been deleted.");
            } catch (Exception e) {
                AlertUtil.showError("Database error", e.getMessage());
            }
        }
    }

    private void handleEdit(Ticket ticket) {
        SceneUtil.switchSceneWithController(listViewTickets, "Views/TicketsCreate.fxml",(TicketCreatorController c) -> c.setTicket(ticket));
        //AlertUtil.showInfo("Edit Ticket", "Edit functionality is not implemented yet.");
    }

    public void onReturnClick(ActionEvent actionEvent) {SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
    }

    public void onBtnCreateTicketClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/TicketsCreate.fxml");

    }
}
