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

public class NewEditEventController {

    @FXML
    private TextField txtEventName;

    @FXML
    private ChoiceBox<?> cBoxStartTime;

    @FXML
    private ChoiceBox<?> cBoxEndTime;

    @FXML
    private ChoiceBox<?> cBoxDay;

    @FXML
    private ChoiceBox<?> cBoxMonth;

    @FXML
    private TextField txtLocation;

    @FXML
    private TextField txtLocationGuidance;

    @FXML
    private TextField txtExtra;

    @FXML
    private TextField txtInformation;

    @FXML
    private ChoiceBox<?> cBoxCoordinator;

    @FXML
    private TextField txtBannerPath;

    @FXML
    public void onSaveEventClick(ActionEvent actionEvent) {
        String name = txtEventName != null ? txtEventName.getText() : "";

        if (name == null || name.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Missing information");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a name for the event.");
            alert.showAndWait();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Event saved");
        alert.setHeaderText(null);
        alert.setContentText("Event \"" + name + "\" has been saved (demo action).");
        alert.showAndWait();

        // After saving, go back to the Events overview.
        navigateToEvents(actionEvent);
    }

    @FXML
    public void onCancelEventClick(ActionEvent actionEvent) {
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

    private void navigateToEvents(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(HelloApplication.class.getResource("Events.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
