package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.User;
import com.example.tickets_app.BLL.Interface.IUserManager;
import com.example.tickets_app.BLL.UserManager;
import com.example.tickets_app.BLL.util.ExceptionHandler;
import com.example.tickets_app.DAL.DAO.UserDAO;
import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import com.example.tickets_app.GUI.util.SessionManager;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class UserController {

    @FXML private ListView<User> listViewUsers;

    private final IUserManager userManager = new UserManager(new UserDAO());
    private final ObservableList<User> userList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        if (!SessionManager.isLoggedIn()) {
            Platform.runLater(SessionManager::redirectToLogin);
            return;
        }
        listViewUsers.setItems(userList);
        listViewUsers.setCellFactory(lv -> new UserListCell(this::handleEdit, this::handleDelete));
        loadUsers();
    }

    private void loadUsers() {
        try {
            userList.setAll(userManager.getAllUsers());
        } catch (ExceptionHandler e) {
            AlertUtil.showError("Database error", "Could not load users: " + e.getMessage());
        }
    }

    private void handleDelete(User user) {
        if (AlertUtil.showConfirmation("Delete User", "Are you sure you want to delete " + user.getFirstName() + " " + user.getLastName() + "?")) {
            try {
                userManager.deleteUser(user.getId());
                userList.remove(user);
                AlertUtil.showInfo("User deleted", user.getFirstName() + " " + user.getLastName() + " has been deleted.");
            } catch (ExceptionHandler e) {
                AlertUtil.showError("Database error", e.getMessage());
            }
        }
    }

    private void handleEdit(User user) {
        SceneUtil.switchSceneWithController(listViewUsers, "Views/Create-Edit-Users.fxml",
                (NewEditUserController c) -> c.setUserToEdit(user));
    }


    @FXML
    public void onBackClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
    }
}