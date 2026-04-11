package com.example.tickets_app.GUI.Controller.Event;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BLL.EventManager;
import com.example.tickets_app.BLL.Interface.IEventManager;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.DAO.EventDAO;
import com.example.tickets_app.GUI.Controller.Misc.AssignCoordinatorController;
import com.example.tickets_app.GUI.Controller.Misc.MenuBarController;
import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import com.example.tickets_app.GUI.util.SessionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

public class EventController {

    @FXML private StackPane rootStack;
    @FXML private ListView<Event> listViewEvents;
    @FXML private TextField txtSearch;

    // fx:include injects sub-controller as fieldName + "Controller"
    @FXML private MenuBarController menuBarController;
    private final IEventManager eventManager = new EventManager(new EventDAO());
    private final ObservableList<Event> eventList = FXCollections.observableArrayList();
    private FilteredList<Event> filteredList;

    @FXML
    public void initialize() {
        if (!SessionManager.isLoggedIn()) {
            Platform.runLater(SessionManager::redirectToLogin);
            return;
        }

        // Wire the hamburger menu
        menuBarController.setup(rootStack, "Events");

        filteredList = new FilteredList<>(eventList, e -> true);
        listViewEvents.setItems(filteredList);
        listViewEvents.setCellFactory(lv ->
                new EventListCell(this::handleDelete, this::handleAssign,
                        this::handleInfo, this::handleEdit));

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            String search = newVal == null ? "" : newVal.toLowerCase();
            filteredList.setPredicate(event -> search.isBlank()
                    || event.getName().toLowerCase().contains(search)
                    || event.getLocation().toLowerCase().contains(search));
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
        if (AlertUtil.showConfirmation("Delete Event",
                "Are you sure you want to delete " + event.getName() + "?")) {
            try {
                eventManager.deleteEvent(event.getId());
                eventList.remove(event);
                AlertUtil.showInfo("Deleted", event.getName() + " has been deleted.");
            } catch (Exception e) {
                AlertUtil.showError("Database error", e.getMessage());
            }
        }
    }

    private void handleAssign(Event event) {
        SceneUtil.switchSceneWithController(listViewEvents, "Views/Misc/AssignCoordinator.fxml",
                (AssignCoordinatorController c) -> c.setEvent(event));
    }

    private void handleInfo(Event event) {
        SceneUtil.switchSceneWithController(listViewEvents, "Views/Event/EventDetail.fxml",
                (EventDetailController c) -> c.setEvent(event));
    }

    private void handleEdit(Event event) {
        SceneUtil.switchSceneWithController(listViewEvents, "Views/Event/New-Edit-Events.fxml",
                (NewEditEventController c) -> c.setEvent(event));
    }
}