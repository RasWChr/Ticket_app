package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.PasswordToggleUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LogInController {

    @FXML private TextField txtUN;
    @FXML private PasswordField txtPsWrd;
    @FXML private TextField txtPsWrdVisible;
    @FXML private Button btnShowPassword;

    private boolean passwordVisible = false;

    @FXML
    public void onLogInClick(ActionEvent actionEvent) {
        String username = txtUN != null ? txtUN.getText() : "";
        String password = PasswordToggleUtil.getPassword(passwordVisible, txtPsWrd, txtPsWrdVisible);

        if (username.isBlank() || password.isBlank()) {
            AlertUtil.showWarning("Missing information", "Please enter Username and Password to log in.");
            return;
        }

        SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
    }

    @FXML
    public void onShowPasswordClick(ActionEvent actionEvent) {
        passwordVisible = PasswordToggleUtil.toggle(passwordVisible, txtPsWrd, txtPsWrdVisible, btnShowPassword);
    }
}