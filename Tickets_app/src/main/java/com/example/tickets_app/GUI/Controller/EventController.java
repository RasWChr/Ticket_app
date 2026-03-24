package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BLL.EventManager;
import com.example.tickets_app.BLL.Interface.IEventManager;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.DAO.EventDAO;
import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import com.example.tickets_app.GUI.util.SessionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class EventController {

    @FXML private ListView<Event> listViewEvents;
    @FXML private TextField txtSearch;

    private final IEventManager eventManager = new EventManager(new EventDAO());
    private final ObservableList<Event> eventList = FXCollections.observableArrayList();
    private FilteredList<Event> filteredList;

    @FXML
    public void initialize() {
        if (!SessionManager.isLoggedIn()) {
            Platform.runLater(SessionManager::redirectToLogin);
            return;
        }

        filteredList = new FilteredList<>(eventList, e -> true);
        listViewEvents.setItems(filteredList);
        listViewEvents.setCellFactory(lv -> new EventListCell(this::handleDelete, this::handleAssign));

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredList.setPredicate(event -> {
                if (newVal == null || newVal.isBlank()) return true;
                String lower = newVal.toLowerCase();
                return event.getName().toLowerCase().contains(lower)
                        || event.getLocation().toLowerCase().contains(lower);
            });
        });

        loadEvents();
    }

    private void loadEvents() {
        try {
            eventList.setAll(eventManager.getEventsForCurrentUser());
        } catch (ExceptionHandler e) {
            AlertUtil.showError("Database error", "Could not load events: " + e.getMessage());
        }
    }

    private void handleDelete(Event event) {
        if (!SessionManager.getLoggedInUser().getRole().equals("Admin")) {
            AlertUtil.showWarning("Access denied", "Only Admins can delete events.");
            return;
        }
        if (AlertUtil.showConfirmation("Delete Event", "Are you sure you want to delete " + event.getName() + "?")) {
            try {
                eventManager.deleteEvent(event.getId());
                eventList.remove(event);
                AlertUtil.showInfo("Deleted", event.getName() + " has been deleted.");
            } catch (ExceptionHandler e) {
                AlertUtil.showError("Database error", e.getMessage());
            }
        }
    }

    private void handleAssign(Event event) {
        if (!SessionManager.getLoggedInUser().getRole().equals("Admin")) {
            AlertUtil.showWarning("Access denied", "Only Admins can assign coordinators.");
            return;
        }
        SceneUtil.switchSceneWithController(listViewEvents, "Views/AssignCoordinator.fxml",
                (AssignCoordinatorController c) -> c.setEvent(event));
    }

    @FXML
    public void onReturnClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
    }
}