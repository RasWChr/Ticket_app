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

public class UserController {

    // Shared handler for all "Edit" buttons in Users.fxml
    @FXML
    public void onEditUserClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit user");
        alert.setHeaderText(null);
        alert.setContentText("Edit user (demo action).");
        alert.showAndWait();
    }

    // Shared handler for all "Delete" buttons in Users.fxml
    @FXML
    public void onDeleteUserClick(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Delete user");
        alert.setHeaderText(null);
        alert.setContentText("User has been deleted (demo action).");
        alert.showAndWait();
    }

    // Handler for the "Back" button in Users.fxml
    @FXML
    public void onBackClick(ActionEvent actionEvent) {
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
