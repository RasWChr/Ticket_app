package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInController {

    @FXML
    private TextField txtUN;

    @FXML
    private TextField txtPsWrd;

    @FXML
    public void onLogInClick(ActionEvent actionEvent) {
        // Simple placeholder login: if both fields are non-empty, navigate to main screen.
        String username = txtUN != null ? txtUN.getText() : "";
        String password = txtPsWrd != null ? txtPsWrd.getText() : "";

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing information");
            alert.setHeaderText(null);
            alert.setContentText("Please enter Username and Password to log in.");
            alert.showAndWait();
            return;
        }

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
