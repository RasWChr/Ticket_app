package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.Event;
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
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.util.StringConverter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TicketListController {

    @FXML private ListView<Ticket> listViewTickets;
    @FXML private ChoiceBox<Event> cBoxEventFilter;

    private final ITicketManager ticketManager = new TicketManager(new EventDAO(), new TicketDAO());
    private final ObservableList<Ticket> ticketList = FXCollections.observableArrayList();
    private FilteredList<Ticket> filteredList;

    private static final Event ALL_EVENTS = new Event(0, "All Events", "", "", "", "", "");

    @FXML
    public void initialize() {
        if (!SessionManager.isLoggedIn()) {
            Platform.runLater(SessionManager::redirectToLogin);
            return;
        }

        cBoxEventFilter.setConverter(new StringConverter<>() {
            @Override public String toString(Event e) { return e != null ? e.getName() : ""; }
            @Override public Event fromString(String s) { return null; }
        });

        loadData();

        filteredList = new FilteredList<>(ticketList, t -> true);
        listViewTickets.setItems(filteredList);
        listViewTickets.setCellFactory(lv ->
                new TicketListCell(this::handleDelete, this::handleEdit, this::handlePreview));

        cBoxEventFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilter(newVal));
    }

    private void loadData() {
        try {
            List<Event> events = ticketManager.getAllEvents();
            Map<Integer, String> eventNameMap = events.stream()
                    .collect(Collectors.toMap(Event::getId, Event::getName));

            List<Ticket> tickets = ticketManager.getAllTickets();
            tickets.forEach(t -> t.setEventName(
                    eventNameMap.getOrDefault(t.getEventID(), "Unknown Event")));
            ticketList.setAll(tickets);

            ObservableList<Event> eventOptions = FXCollections.observableArrayList();
            eventOptions.add(ALL_EVENTS);
            eventOptions.addAll(events);
            cBoxEventFilter.setItems(eventOptions);
            cBoxEventFilter.setValue(ALL_EVENTS);

        } catch (ExceptionHandler e) {
            AlertUtil.showError("Database error", "Could not load data: " + e.getMessage());
        }
    }

    private void applyFilter(Event selected) {
        if (selected == null || selected == ALL_EVENTS) {
            filteredList.setPredicate(null);
        } else {
            int selectedId = selected.getId();
            filteredList.setPredicate(t -> t.getEventID() == selectedId);
        }
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

    private void handlePreview(Ticket ticket) {
        // Find the matching event from the already-loaded list
        Event matchingEvent = cBoxEventFilter.getItems().stream()
                .filter(e -> e != ALL_EVENTS && e.getId() == ticket.getEventID())
                .findFirst()
                .orElse(null);

        SceneUtil.switchSceneWithController(listViewTickets, "Views/TicketPreview.fxml",
                (TicketPreviewController c) -> c.setTicket(ticket, matchingEvent));
    }

    public void onReturnClick(ActionEvent actionEvent) {SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
    }

    public void onBtnCreateTicketClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/TicketsCreate.fxml");

    }
}
