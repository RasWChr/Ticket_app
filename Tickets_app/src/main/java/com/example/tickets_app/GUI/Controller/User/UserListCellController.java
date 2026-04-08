package com.example.tickets_app.GUI.Controller.User;

import com.example.tickets_app.BE.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.function.Consumer;

public class UserListCellController {

    @FXML private Label lblUserInfo;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;

    private Consumer<User> onDelete;
    private Consumer<User> onEdit;
    private User user;

    public void setUser(User user, Consumer<User> onEdit, Consumer<User> onDelete) {
        this.user = user;
        this.onEdit = onEdit;
        this.onDelete = onDelete;
        lblUserInfo.setText(user.getFirstName() + " " + user.getLastName() + " (" + user.getRole() + ")");
    }

    @FXML
    public void onDeleteClick(ActionEvent actionEvent) {
        if (onDelete != null) {
            onDelete.accept(user);
        }
    }

    @FXML
    public void onEditClick(ActionEvent actionEvent) {
        if (onEdit != null) {
            onEdit.accept(user);
        }
    }
}