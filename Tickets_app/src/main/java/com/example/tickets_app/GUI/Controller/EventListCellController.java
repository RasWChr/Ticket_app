package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.GUI.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class EventListCellController {

    @FXML private Label lblEventName;
    @FXML private Label lblEventDetails;
    @FXML private Label lblStatus;
    @FXML private Button btnOptions;

    private Consumer<Event> onDelete;
    private Consumer<Event> onAssign;
    private Consumer<Event> onInfo;
    private Consumer<Event> onEdit;
    private Event event;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public void setEvent(Event event, Consumer<Event> onDelete,
                         Consumer<Event> onAssign, Consumer<Event> onInfo, Consumer<Event> onEdit) {
        this.event = event;
        this.onDelete = onDelete;
        this.onAssign = onAssign;
        this.onInfo = onInfo;
        this.onEdit = onEdit;

        lblEventName.setText(event.getName());
        lblEventDetails.setText(event.getStartDateTime() + " | " + event.getLocation());

        setStatus();
        buildContextMenu();
    }

    private void setStatus() {
        try {
            LocalDateTime start = LocalDateTime.parse(event.getStartDateTime(), FORMATTER);
            LocalDateTime now = LocalDateTime.now();

            if (start.isBefore(now)) {
                lblStatus.setText("● Held");
                lblStatus.getStyleClass().add("status-held");
            } else if (start.isBefore(now.plusHours(24))) {
                lblStatus.setText("● Starting Soon");
                lblStatus.getStyleClass().add("status-soon");
            } else {
                lblStatus.setText("● Upcoming");
                lblStatus.getStyleClass().add("status-upcoming");
            }
        } catch (Exception e) {
            lblStatus.setText("");
        }
    }

    private void buildContextMenu() {
        ContextMenu menu = new ContextMenu();
        menu.getStyleClass().add("dark-context-menu");

        String role = SessionManager.getLoggedInUser().getRole();

        // Info - visible to everyone
        MenuItem itemInfo = new MenuItem("ℹ Info");
        itemInfo.setOnAction(e -> { if (onInfo != null) onInfo.accept(event); });

        MenuItem itemDelete = new MenuItem("🗑 Delete");
        itemDelete.setOnAction(e -> { if (onDelete != null) onDelete.accept(event); });

        menu.getItems().addAll(itemInfo, itemDelete);

        // Coordinator-only options
        if (role.equals("Coordinator")) {
            menu.getItems().add(new SeparatorMenuItem());

            MenuItem itemEdit = new MenuItem("✏ Edit");
            itemEdit.setOnAction(e -> { if (onEdit != null) onEdit.accept(event); });

            menu.getItems().add(itemEdit);
        }
        // Admin-only options
        if (role.equals("Admin")) {
            menu.getItems().add(new SeparatorMenuItem());

            MenuItem itemAssign = new MenuItem("👤 Assign Coordinator");
            itemAssign.setOnAction(e -> { if (onAssign != null) onAssign.accept(event); });

            menu.getItems().add(itemAssign);
        }

        btnOptions.setOnAction(e ->
                menu.show(btnOptions,
                        btnOptions.localToScreen(0, 0).getX(),
                        btnOptions.localToScreen(0, 0).getY() + btnOptions.getHeight()));
    }

    @FXML
    public void onOptionsClick(ActionEvent actionEvent) {
        // Handled by buildContextMenu
    }
}