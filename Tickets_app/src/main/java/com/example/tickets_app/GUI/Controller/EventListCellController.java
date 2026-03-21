package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.GUI.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.function.Consumer;

public class EventListCellController {

    @FXML private Label lblEventName;
    @FXML private Label lblEventDetails;
    @FXML private Button btnDelete;
    @FXML private Button btnAssign;

    private Consumer<Event> onDelete;
    private Consumer<Event> onAssign;
    private Event event;

    public void setEvent(Event event, Consumer<Event> onDelete, Consumer<Event> onAssign) {
        this.event = event;
        this.onDelete = onDelete;
        this.onAssign = onAssign;

        lblEventName.setText(event.getName());
        lblEventDetails.setText(event.getStartDateTime() + " | " + event.getLocation());

        String role = SessionManager.getLoggedInUser().getRole();
        btnDelete.setVisible(role.equals("Admin"));
        btnDelete.setManaged(role.equals("Admin"));
        btnAssign.setVisible(role.equals("Admin"));
        btnAssign.setManaged(role.equals("Admin"));
    }

    @FXML
    public void onDeleteClick(ActionEvent actionEvent) {
        if (onDelete != null) onDelete.accept(event);
    }

    @FXML
    public void onAssignClick(ActionEvent actionEvent) {
        if (onAssign != null) onAssign.accept(event);
    }
}