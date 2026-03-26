package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.User;
import com.example.tickets_app.GUI.util.SceneUtil;
import com.example.tickets_app.GUI.util.SessionManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MainController {

    @FXML private Label lblUser;
    @FXML private Button btnCreateU;
    @FXML private Button btnManageU;
    @FXML private Button btnEvent;
    @FXML private Button btnCreateE;
    @FXML private Button btnCreateT;

    @FXML
    public void initialize() {
        if (!SessionManager.isLoggedIn()) {
            Platform.runLater(SessionManager::redirectToLogin);
            return;
        }

        User user = SessionManager.getLoggedInUser();
        String role = user.getRole();

        if (role.equals("Admin")) {
            btnCreateU.setVisible(true);
            btnCreateU.setManaged(true);
            btnManageU.setVisible(true);
            btnManageU.setManaged(true);
            btnEvent.setVisible(true);   // Admin can see events to delete and assign coordinators
            btnEvent.setManaged(true);
            btnCreateE.setVisible(false); // Admin cannot create events
            btnCreateE.setManaged(false);
            btnCreateT.setVisible(false); // Admin cannot create tickets
            btnCreateT.setManaged(false);

        } else if (role.equals("Coordinator")) {
            btnCreateU.setVisible(false); // Coordinator cannot manage users
            btnCreateU.setManaged(false);
            btnManageU.setVisible(false);
            btnManageU.setManaged(false);
            btnEvent.setVisible(true);
            btnEvent.setManaged(true);
            btnCreateE.setVisible(true);
            btnCreateE.setManaged(true);
            btnCreateT.setVisible(true);
            btnCreateT.setManaged(true);
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
        SessionManager.clearSession();
        SceneUtil.switchScene(actionEvent, "Views/Log-in.fxml");
    }
}