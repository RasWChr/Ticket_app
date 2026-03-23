package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.User;
import com.example.tickets_app.BLL.Interface.IUserManager;
import com.example.tickets_app.BLL.UserManager;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.DAO.UserDAO;
import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.PasswordToggleUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import com.example.tickets_app.GUI.util.SessionManager;
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
    private final IUserManager userManager = new UserManager(new UserDAO());

    @FXML
    public void onLogInClick(ActionEvent actionEvent) {
        String email = txtUN != null ? txtUN.getText() : "";
        String password = PasswordToggleUtil.getPassword(passwordVisible, txtPsWrd, txtPsWrdVisible);

        try {
            User user = userManager.login(email, password);
            SessionManager.setLoggedInUser(user);
            SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
        } catch (IllegalArgumentException e) {
            AlertUtil.showWarning("Login failed", e.getMessage());
        } catch (ExceptionHandler e) {
            AlertUtil.showError("Database error", e.getMessage());
        }
    }

    @FXML
    public void onShowPasswordClick(ActionEvent actionEvent) {
        passwordVisible = PasswordToggleUtil.toggle(passwordVisible, txtPsWrd, txtPsWrdVisible, btnShowPassword);
    }
}