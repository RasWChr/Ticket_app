package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class TicketController {

    @FXML private ChoiceBox<?> cBoxEvent;
    @FXML private TextField txtFirstNT;
    @FXML private TextField txtLastNT;
    @FXML private TextField txtEmailT;
    @FXML private TextField txtPhoneT;

    @FXML
    public void onBtnCancelClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
    }

    @FXML
    public void onPTicketClick(ActionEvent actionEvent) {
        String firstName = txtFirstNT != null ? txtFirstNT.getText() : "";
        String lastName = txtLastNT != null ? txtLastNT.getText() : "";
        String email = txtEmailT != null ? txtEmailT.getText() : "";

        if (firstName.isBlank() || lastName.isBlank() || email.isBlank()) {
            AlertUtil.showWarning("Missing information", "Please fill in first name, last name and email before printing the ticket.");
            return;
        }

        AlertUtil.showInfo("Ticket created", "Ticket has been created for " + firstName + " " + lastName + ".");
    }
}