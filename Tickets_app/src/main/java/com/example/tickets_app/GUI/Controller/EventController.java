package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class EventController {

    // Shared handler for all "Delete" buttons in Events.fxml
    @FXML
    public void onDeleteEventClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Delete event");
        alert.setHeaderText(null);
        alert.setContentText("Event has been deleted (demo action).");
        alert.showAndWait();
    }

    // Shared handler for all "Manage" buttons in Events.fxml
    @FXML
    public void onManageEventClick(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(HelloApplication.class.getResource("New-Edit-Events.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onReturnClick(ActionEvent actionEvent) {
        // Cancel and go back to main screen.
        try {
            Parent root = FXMLLoader.load(HelloApplication.class.getResource("Main-Screen.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
