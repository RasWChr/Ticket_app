package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private void switchScene(ActionEvent event, String fxmlName) {
        try {
            Parent root = FXMLLoader.load(HelloApplication.class.getResource(fxmlName));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onCreateUClick(ActionEvent actionEvent) {
        switchScene(actionEvent, "Create-Edit-Users.fxml");
    }

    public void onManageUClick(ActionEvent actionEvent) {
        switchScene(actionEvent, "Users.fxml");
    }

    public void onEventClick(ActionEvent actionEvent) {
        switchScene(actionEvent, "Events.fxml");
    }

    public void onCreateEClick(ActionEvent actionEvent) {
        switchScene(actionEvent, "New-Edit-Events.fxml");
    }

    public void onCreateTClick(ActionEvent actionEvent) {
        switchScene(actionEvent, "Tickets.fxml");
    }

    public void onLogOutClick(ActionEvent actionEvent) {
        switchScene(actionEvent, "Log-in.fxml");
    }
}
