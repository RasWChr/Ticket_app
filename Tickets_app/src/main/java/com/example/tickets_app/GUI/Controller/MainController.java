package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.User;
import com.example.tickets_app.GUI.Controller.Misc.MenuBarController;
import com.example.tickets_app.GUI.util.SceneUtil;
import com.example.tickets_app.GUI.util.SessionManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MainController {


    @FXML private Button btnPrintT;
    @FXML private Label lblUser;
    @FXML private Button btnCreateU;
    @FXML private Button btnManageU;
    @FXML private Button btnEvent;
    @FXML private Button btnCreateE;
    @FXML private Button btnCreateT;

    @FXML private StackPane rootStack;
    @FXML private MenuBarController menuBarController;

    @FXML
    public void initialize() {
        if (!SessionManager.isLoggedIn()) {
            Platform.runLater(SessionManager::redirectToLogin);
            return;
        }
        menuBarController.setup(rootStack, "Home");
        // Hide back button on main screen
        menuBarController.hideBackButton();



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
            btnPrintT.setVisible(false);

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
            btnPrintT.setVisible(true);
        }
    }



    public void onCreateUClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/User/Create-Edit-Users.fxml");
    }

    public void onManageUClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/User/Users.fxml");
    }

    public void onEventClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Event/Events.fxml");
    }

    public void onCreateEClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Event/New-Edit-Events.fxml");
    }

    public void onCreateTClick(ActionEvent actionEvent) {SceneUtil.switchScene(actionEvent, "Views/Ticket/TicketList.fxml");
    }

    public void onPrintTClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Ticket/Tickets.fxml");
    }

    public void onLogOutClick(ActionEvent actionEvent) {
        SessionManager.clearSession();
        SceneUtil.switchScene(actionEvent, "Views/Misc/Log-in.fxml");
    }


}