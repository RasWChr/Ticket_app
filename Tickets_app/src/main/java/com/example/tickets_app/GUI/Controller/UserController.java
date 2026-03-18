package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class UserController {

    @FXML
    public void onEditUserClick(ActionEvent actionEvent) {
        AlertUtil.showInfo("Edit user", "Edit user (demo action).");
    }

    @FXML
    public void onDeleteUserClick(ActionEvent actionEvent) {
        AlertUtil.showInfo("Delete user", "User has been deleted (demo action).");
    }

    @FXML
    public void onBackClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
    }
}