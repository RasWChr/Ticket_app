package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.User;
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

public class NewEditUserController extends BaseController{

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
    private User userToEdit = null; // null = create mode, non-null = edit mode

    //Kaldet på af userController når man laver om på en existerene user
    public void setUserToEdit(User user) {
        this.userToEdit = user;
        txtFirstNU.setText(user.getFirstName());
        txtLastNU.setText(user.getLastName());
        txtEmailU.setText(user.getEmail());
        txtPhoneU.setText(user.getPhoneNumber());
        selectedRole = user.getRole();

        // Hide password field in edit mode (we're not changing the password here)
        txtPassword.setManaged(false);
        txtPassword.setVisible(false);
        txtPasswordVisible.setManaged(false);
        txtPasswordVisible.setVisible(false);
        btnShowPassword.setManaged(false);
        btnShowPassword.setVisible(false);
    }

    @FXML
    public void onCancelUClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
    }

    @FXML
    public void onUserCEClick(ActionEvent actionEvent) {
        String firstName = Method(txtFirstNU);
        String lastName = Method(txtLastNU);
        String email = Method(txtEmailU);
        String phone = Method(txtPhoneU);


        if (selectedRole == null) {
            AlertUtil.showWarning("Missing role", "Please select a role for the user.");
            return;
        }
        try {
            if (userToEdit == null) {
                // CREATE mode
                String password = PasswordToggleUtil.getPassword(passwordVisible, txtPassword, txtPasswordVisible);
                if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
                    AlertUtil.showWarning("Missing information", "Please fill in first name, last name, email and password.");
                    return;
                }
                userManager.createUser(firstName, lastName, email, phone, password, selectedRole);
                AlertUtil.showInfo("User created", firstName + " " + lastName + " (" + selectedRole + ") has been created.");
                clearFields();

            } else {
                // EDIT mode
                userManager.editUser(userToEdit.getId(), firstName, lastName, email, phone, selectedRole);
                AlertUtil.showInfo("User updated", firstName + " " + lastName + " has been updated.");
                SceneUtil.switchScene(actionEvent, "Views/Users.fxml");
            }

        } catch (IllegalArgumentException e) {
            AlertUtil.showWarning("Invalid input", e.getMessage());
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