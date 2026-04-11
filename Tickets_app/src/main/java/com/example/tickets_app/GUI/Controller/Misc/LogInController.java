package com.example.tickets_app.GUI.Controller.Misc;

import com.example.tickets_app.BE.User;
import com.example.tickets_app.BLL.Interface.IUserManager;
import com.example.tickets_app.BLL.UserManager;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.DAO.UserDAO;
import com.example.tickets_app.GUI.util.PasswordToggleUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import com.example.tickets_app.GUI.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LogInController {

    @FXML private TextField     txtUN;
    @FXML private PasswordField txtPsWrd;
    @FXML private TextField     txtPsWrdVisible;
    @FXML private Button        btnShowPassword;
    @FXML private Button        btnLogIn;
    @FXML private Label         lblError;

    private boolean passwordVisible = false;
    private final IUserManager userManager = new UserManager(new UserDAO());

    @FXML
    public void initialize() {

        // ENTER triggers login from all fields
        txtUN.setOnAction(e -> btnLogIn.fire());
        txtPsWrd.setOnAction(e -> btnLogIn.fire());
        txtPsWrdVisible.setOnAction(e -> btnLogIn.fire());
    }

    @FXML
    public void onLogInClick(ActionEvent actionEvent) {
        String email    = txtUN != null ? txtUN.getText().trim() : "";
        String password = PasswordToggleUtil.getPassword(passwordVisible, txtPsWrd, txtPsWrdVisible);

        clearErrors();

        if (email.isBlank() && password.isBlank()) {
            setFieldError(txtUN, true);
            setFieldError(txtPsWrd, true);
            showError("Please enter your email and password.");
            return;
        }
        if (email.isBlank()) {
            setFieldError(txtUN, true);
            showError("Please enter your email.");
            return;
        }
        if (password.isBlank()) {
            setFieldError(txtPsWrd, true);
            showError("Please enter your password.");
            return;
        }

        try {
            User user = userManager.login(email, password);
            SessionManager.setLoggedInUser(user);
            SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");

        } catch (IllegalArgumentException e) {
            String msg = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
            if (msg.contains("email")) {
                setFieldError(txtUN, true);
            } else {
                setFieldError(txtPsWrd, true);
            }
            showError("Incorrect email or password.");

        } catch (ExceptionHandler e) {
            setFieldError(txtUN, true);
            setFieldError(txtPsWrd, true);
            showError("Incorrect email or password.");
        }
    }

    @FXML
    public void onShowPasswordClick(ActionEvent actionEvent) {
        passwordVisible = PasswordToggleUtil.toggle(
                passwordVisible, txtPsWrd, txtPsWrdVisible, btnShowPassword);
    }

    private void showError(String message) {
        lblError.setText(message);
        lblError.setVisible(true);
        lblError.setManaged(true);
    }

    private void clearErrors() {
        lblError.setVisible(false);
        lblError.setManaged(false);
        lblError.setText("");
        setFieldError(txtUN, false);
        setFieldError(txtPsWrd, false);
        setFieldError(txtPsWrdVisible, false);
    }

    private void setFieldError(javafx.scene.control.Control field, boolean error) {
        if (field == null) return;
        if (error) {
            if (!field.getStyleClass().contains("field-error"))
                field.getStyleClass().add("field-error");
        } else {
            field.getStyleClass().remove("field-error");
        }
    }
}