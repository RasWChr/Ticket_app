package com.example.tickets_app.GUI.Controller.Misc;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.User;
import com.example.tickets_app.BLL.EventManager;
import com.example.tickets_app.BLL.Interface.IEventManager;
import com.example.tickets_app.BLL.Interface.IUserManager;
import com.example.tickets_app.BLL.UserManager;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.DAO.EventDAO;
import com.example.tickets_app.DAL.DAO.UserDAO;
import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.List;

public class AssignCoordinatorController {

    @FXML private Label lblEventName;
    @FXML private ListView<User> listAssigned;
    @FXML private ListView<User> listAvailable;

    private final IEventManager eventManager = new EventManager(new EventDAO());
    private final IUserManager userManager = new UserManager(new UserDAO());

    private final ObservableList<User> assignedList = FXCollections.observableArrayList();
    private final ObservableList<User> availableList = FXCollections.observableArrayList();

    private Event event;

    public void setEvent(Event event) {
        this.event = event;
        lblEventName.setText("Assign Coordinators to: " + event.getName());
        loadLists();
    }

    @FXML
    public void initialize() {
        listAssigned.setItems(assignedList);
        listAvailable.setItems(availableList);

        listAssigned.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? null : user.getFirstName() + " " + user.getLastName());
                setTextFill(javafx.scene.paint.Color.WHITE);
            }
        });

        listAvailable.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty || user == null ? null : user.getFirstName() + " " + user.getLastName());
                setTextFill(javafx.scene.paint.Color.WHITE);
            }
        });
    }

    private void loadLists() {
        try {
            List<User> assigned = eventManager.getCoordinatorsForEvent(event.getId());
            List<User> allCoordinators = userManager.getAllUsers().stream()
                    .filter(u -> u.getRole().equals("Coordinator"))
                    .toList();

            assignedList.setAll(assigned);

            List<Integer> assignedIds = assigned.stream().map(User::getId).toList();
            availableList.setAll(allCoordinators.stream()
                    .filter(u -> !assignedIds.contains(u.getId()))
                    .toList());

        } catch (ExceptionHandler e) {
            AlertUtil.showError("Database error", "Could not load coordinators: " + e.getMessage());
        }
    }

    @FXML
    public void onAssignClick(ActionEvent actionEvent) {
        User selected = listAvailable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("No selection", "Please select a coordinator to assign.");
            return;
        }
        try {
            eventManager.assignCoordinator(event.getId(), selected.getId());
            loadLists();
            AlertUtil.showInfo("Assigned", selected.getFirstName() + " " + selected.getLastName() + " has been assigned to " + event.getName() + ".");
        } catch (ExceptionHandler e) {
            AlertUtil.showError("Database error", e.getMessage());
        }
    }

    @FXML
    public void onRemoveClick(ActionEvent actionEvent) {
        User selected = listAssigned.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertUtil.showWarning("No selection", "Please select a coordinator to remove.");
            return;
        }
        if (AlertUtil.showConfirmation("Remove Coordinator", "Remove " + selected.getFirstName() + " " + selected.getLastName() + " from this event?")) {
            try {
                eventManager.removeCoordinator(event.getId(), selected.getId());
                loadLists();
            } catch (ExceptionHandler e) {
                AlertUtil.showError("Database error", e.getMessage());
            }
        }
    }

    @FXML
    public void onBackClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Event/Events.fxml");
    }
}