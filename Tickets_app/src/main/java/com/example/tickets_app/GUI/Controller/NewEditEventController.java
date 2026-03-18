package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class NewEditEventController {

    @FXML private TextField txtEventName;
    @FXML private ChoiceBox<?> cBoxStartTime;
    @FXML private ChoiceBox<?> cBoxEndTime;
    @FXML private ChoiceBox<?> cBoxDay;
    @FXML private ChoiceBox<?> cBoxMonth;
    @FXML private TextField txtLocation;
    @FXML private TextField txtLocationGuidance;
    @FXML private TextField txtExtra;
    @FXML private TextField txtInformation;
    @FXML private ChoiceBox<?> cBoxCoordinator;
    @FXML private TextField txtBannerPath;

    @FXML
    public void onSaveEventClick(ActionEvent actionEvent) {
        String name = txtEventName != null ? txtEventName.getText() : "";

        if (name.isBlank()) {
            AlertUtil.showWarning("Missing information", "Please enter a name for the event.");
            return;
        }

        AlertUtil.showInfo("Event saved", "Event \"" + name + "\" has been saved (demo action).");
        SceneUtil.switchScene(actionEvent, "Views/Events.fxml");
    }

    @FXML
    public void onCancelEventClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
    }
}