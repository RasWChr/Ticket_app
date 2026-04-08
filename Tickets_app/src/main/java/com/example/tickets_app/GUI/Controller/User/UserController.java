package com.example.tickets_app.GUI.Controller.User;

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
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class UserController {

    @FXML private ListView<User> listViewUsers;
    @FXML private TextField txtSearch;
    @FXML private ChoiceBox<String> cBoxRoleFilter;

    private final IUserManager userManager = new UserManager(new UserDAO());
    private final ObservableList<User> userList = FXCollections.observableArrayList();
    private FilteredList<User> filteredList;

    @FXML
    public void initialize() {
        if (!SessionManager.isLoggedIn()) {
            Platform.runLater(SessionManager::redirectToLogin);
            return;
        }

        cBoxRoleFilter.getItems().addAll("All", "Admin", "Coordinator");
        cBoxRoleFilter.setValue("All");

        filteredList = new FilteredList<>(userList, u -> true);
        listViewUsers.setItems(filteredList);
        listViewUsers.setCellFactory(lv -> new UserListCell(this::handleEdit, this::handleDelete));

        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
        cBoxRoleFilter.valueProperty().addListener((obs, oldVal, newVal) -> applyFilter());

        loadUsers();
    }

    private void applyFilter() {
        String search = txtSearch.getText() == null ? "" : txtSearch.getText().toLowerCase();
        String role = cBoxRoleFilter.getValue();

        filteredList.setPredicate(user -> {
            boolean matchesSearch = search.isBlank()
                    || user.getFirstName().toLowerCase().contains(search)
                    || user.getLastName().toLowerCase().contains(search)
                    || user.getEmail().toLowerCase().contains(search);

            boolean matchesRole = role == null || role.equals("All")
                    || user.getRole().equals(role);

            return matchesSearch && matchesRole;
        });
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
        SceneUtil.switchSceneWithController(listViewUsers, "Views/User/Create-Edit-Users.fxml",
                (NewEditUserController c) -> c.setUserToEdit(user));
    }

    @FXML
    public void onBackClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
    }
}