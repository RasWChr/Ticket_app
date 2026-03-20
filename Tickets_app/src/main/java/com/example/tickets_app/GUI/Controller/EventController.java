package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class EventController {

    @FXML
    public void onDeleteEventClick(ActionEvent actionEvent) {
        AlertUtil.showInfo("Delete event", "Event has been deleted (demo action).");
    }

    @FXML
    public void onManageEventClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Manage-Events.fxml");
    }

    @FXML
    public void onReturnClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
    }


}