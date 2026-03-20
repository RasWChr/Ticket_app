package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.GUI.util.SceneUtil;
import com.example.tickets_app.GUI.util.SessionManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainController {

    @FXML
    public void initialize() {
        if (!SessionManager.isLoggedIn()){
            Platform.runLater(SessionManager::redirectToLogin);
        }
    }

    public void onCreateUClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Create-Edit-Users.fxml");
    }

    public void onManageUClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Users.fxml");
    }

    public void onEventClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Events.fxml");
    }

    public void onCreateEClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/New-Edit-Events.fxml");
    }

    public void onCreateTClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Tickets.fxml");
    }

    public void onLogOutClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Log-in.fxml");
    }
}