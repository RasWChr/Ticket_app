package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import com.example.tickets_app.GUI.util.SessionManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class EventController {

    @FXML
    public void initialize() {
        if (!SessionManager.isLoggedIn()) {
            Platform.runLater(SessionManager::redirectToLogin);
        }
    }

    @FXML
    public void onDeleteEventClick(ActionEvent actionEvent) {
        if (!SessionManager.getLoggedInUser().getRole().equals("Admin")) {
            AlertUtil.showWarning("Access denied", "Only Admins can delete events.");
            return;
        }
        AlertUtil.showInfo("Delete event", "Event has been deleted (demo action).");
    }

    @FXML
    public void onManageEventClick(ActionEvent actionEvent) {
        if (!SessionManager.getLoggedInUser().getRole().equals("Coordinator")) {
            AlertUtil.showWarning("Access denied", "Only Coordinators can manage events.");
            return;
        }
        SceneUtil.switchScene(actionEvent, "Views/New-Edit-Events.fxml");
    }

    @FXML
    public void onReturnClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
    }
}