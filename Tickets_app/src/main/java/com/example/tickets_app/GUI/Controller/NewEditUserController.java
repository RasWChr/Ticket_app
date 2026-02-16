package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class NewEditUserController {

    @FXML
    private TextField txtFirstNU;

    @FXML
    private TextField txtLastNU;

    @FXML
    private TextField txtEmailU;

    @FXML
    private TextField txtPhoneU;

    @FXML
    private Button btnCoordinator;

    @FXML
    private Button btnAdmin;

    private String selectedRole;

    @FXML
    public void onCancelUClick(ActionEvent actionEvent) {
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

    @FXML
    public void onUserCEClick(ActionEvent actionEvent) {
        // Very simple validation and confirmation dialog.
        String firstName = txtFirstNU != null ? txtFirstNU.getText() : "";
        String lastName = txtLastNU != null ? txtLastNU.getText() : "";
        String email = txtEmailU != null ? txtEmailU.getText() : "";

        if (firstName == null || firstName.isBlank()
                || lastName == null || lastName.isBlank()
                || email == null || email.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in at least first name, last name and email before creating the user.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("User created");
        alert.setHeaderText(null);
        String roleText = selectedRole != null ? " (" + selectedRole + ")" : "";
        alert.setContentText("User " + firstName + " " + lastName + roleText + " has been created.");
        alert.showAndWait();
    }

    @FXML
    public void onCoordinatorClick(ActionEvent actionEvent) {
        selectedRole = "Coordinator";

        if (btnCoordinator != null) {
            btnCoordinator.setDefaultButton(true);
        }
        if (btnAdmin != null) {
            btnAdmin.setDefaultButton(false);
        }
    }

    @FXML
    public void onAdminClick(ActionEvent actionEvent) {
        selectedRole = "Admin";

        if (btnAdmin != null) {
            btnAdmin.setDefaultButton(true);
        }
        if (btnCoordinator != null) {
            btnCoordinator.setDefaultButton(false);
        }
    }
}
