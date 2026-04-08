package com.example.tickets_app.GUI.Controller.Event;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.GUI.util.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventDetailController {

    @FXML private Label lblName;
    @FXML private Label lblStart;
    @FXML private Label lblEnd;
    @FXML private Label lblLocation;
    @FXML private Label lblLocationGuidance;
    @FXML private Label lblStatus;
    @FXML private Label lblNotes;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public void setEvent(Event event) {
        lblName.setText(event.getName());
        lblStart.setText(event.getStartDateTime());
        lblEnd.setText(event.getEndDateTime() != null ? event.getEndDateTime() : "—");
        lblLocation.setText(event.getLocation());
        lblLocationGuidance.setText(
                event.getLocationGuidance() != null ? event.getLocationGuidance() : "—");
        lblNotes.setText(event.getNotes() != null ? event.getNotes() : "—");

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
            lblStatus.setText("Unknown");
        }
    }

    @FXML
    public void onBackClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Event/Events.fxml");
    }
}