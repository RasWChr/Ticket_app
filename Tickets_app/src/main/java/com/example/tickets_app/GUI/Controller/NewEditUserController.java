package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BLL.Interface.IUserManager;
import com.example.tickets_app.BLL.UserManager;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.DAO.UserDAO;
import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.PasswordToggleUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class NewEditUserController {

    @FXML private TextField txtFirstNU;
    @FXML private TextField txtLastNU;
    @FXML private TextField txtEmailU;
    @FXML private TextField txtPhoneU;
    @FXML private PasswordField txtPassword;
    @FXML private TextField txtPasswordVisible;
    @FXML private Button btnShowPassword;
    @FXML private Button btnCoordinator;
    @FXML private Button btnAdmin;

    private String selectedRole;
    private boolean passwordVisible = false;
    private final IUserManager userManager = new UserManager(new UserDAO());

    @FXML
    public void onCancelUClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
    }

    @FXML
    public void onUserCEClick(ActionEvent actionEvent) {
        String firstName = txtFirstNU != null ? txtFirstNU.getText() : "";
        String lastName = txtLastNU != null ? txtLastNU.getText() : "";
        String email = txtEmailU != null ? txtEmailU.getText() : "";
        String phone = txtPhoneU != null ? txtPhoneU.getText() : "";
        String password = PasswordToggleUtil.getPassword(passwordVisible, txtPassword, txtPasswordVisible);

        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
            AlertUtil.showWarning("Missing information", "Please fill in first name, last name, email and password.");
            return;
        }

        if (selectedRole == null) {
            AlertUtil.showWarning("Missing role", "Please select a role for the user.");
            return;
        }

        try {
            userManager.createUser(firstName, lastName, email, phone, password, selectedRole);
            AlertUtil.showInfo("User created", "User " + firstName + " " + lastName + " (" + selectedRole + ") has been created.");
            clearFields();
        } catch (IllegalArgumentException e) {
            AlertUtil.showWarning("Duplicate email", e.getMessage());
        } catch (ExceptionHandler e) {
            AlertUtil.showError("Database error", e.getMessage());
        }
    }

    @FXML
    public void onCoordinatorClick(ActionEvent actionEvent) {
        selectedRole = "Coordinator";
    }

    @FXML
    public void onAdminClick(ActionEvent actionEvent) {
        selectedRole = "Admin";
    }

    @FXML
    public void onShowPasswordClick(ActionEvent actionEvent) {
        passwordVisible = PasswordToggleUtil.toggle(passwordVisible, txtPassword, txtPasswordVisible, btnShowPassword);
    }
    private void clearFields() {
        txtFirstNU.clear();
        txtLastNU.clear();
        txtEmailU.clear();
        txtPhoneU.clear();
        txtPassword.clear();
        txtPasswordVisible.clear();
        selectedRole = null;

        // Reset password field visibility back to hidden
        txtPassword.setManaged(true);
        txtPassword.setVisible(true);
        txtPasswordVisible.setManaged(false);
        txtPasswordVisible.setVisible(false);
        btnShowPassword.setText("Show");
        passwordVisible = false;
    }
}