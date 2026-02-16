package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class TicketController {

    @FXML
    private ChoiceBox<?> cBoxEvent;

    @FXML
    private TextField txtFirstNT;

    @FXML
    private TextField txtLastNT;

    @FXML
    private TextField txtEmailT;

    @FXML
    private TextField txtPhoneT;

    @FXML
    public void onSTicketClick(ActionEvent actionEvent) {
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
    public void onPTicketClick(ActionEvent actionEvent) {
        // Very simple validation and confirmation dialog.
        String firstName = txtFirstNT != null ? txtFirstNT.getText() : "";
        String lastName = txtLastNT != null ? txtLastNT.getText() : "";
        String email = txtEmailT != null ? txtEmailT.getText() : "";

        if (firstName == null || firstName.isBlank()
                || lastName == null || lastName.isBlank()
                || email == null || email.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing information");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in at least first name, last name and email before printing the ticket.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ticket created");
        alert.setHeaderText(null);
        alert.setContentText("Ticket has been created for " + firstName + " " + lastName + ".");
        alert.showAndWait();
    }
}
