package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.User;
import com.example.tickets_app.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.function.Consumer;

public class UserListCell extends ListCell<User> {

    private final Consumer<User> onEdit;
    private final Consumer<User> onDelete;

    public UserListCell(Consumer<User> onEdit, Consumer<User> onDelete) {
        this.onEdit = onEdit;
        this.onDelete = onDelete;
    }

    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);

        if (empty || user == null) {
            setText(null);
            setGraphic(null);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("Views/UserListCell.fxml"));
                HBox root = loader.load();
                UserListCellController controller = loader.getController();
                controller.setUser(user, onEdit, onDelete);
                setGraphic(root);
                setText(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}