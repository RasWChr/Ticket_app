package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BLL.EventManager;
import com.example.tickets_app.BLL.Interface.IEventManager;
import com.example.tickets_app.DAL.DAO.EventDAO;
import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class NewEditEventController {

    @FXML private TextField txtEventName;
    @FXML private TextField txtLocation;
    @FXML private TextField txtLocationGuidance;
    @FXML private TextField txtExtra;
    @FXML private TextField txtInformation;

    // fx:include injects the sub-controller via fieldName + "Controller" suffix
    @FXML private DateTimePickerController startPickerController;
    @FXML private DateTimePickerController endPickerController;

    private final IEventManager eventManager = new EventManager(new EventDAO());
    private Event eventToEdit = null;

    public void setEventToEdit(Event event) {
        this.eventToEdit = event;
        txtEventName.setText(event.getName());
        txtLocation.setText(event.getLocation());
        txtLocationGuidance.setText(
                event.getLocationGuidance() != null ? event.getLocationGuidance() : "");
        txtInformation.setText(event.getNotes() != null ? event.getNotes() : "");
        startPickerController.setDateTime(event.getStartDateTime());
        endPickerController.setDateTime(event.getEndDateTime());
    }

    // Keep old setEvent as an alias so existing callers don't break
    public void setEvent(Event event) {
        setEventToEdit(event);
    }

    @FXML
    public void onSaveEventClick(ActionEvent actionEvent) {
        String name             = txtEventName        != null ? txtEventName.getText()        : "";
        String location         = txtLocation         != null ? txtLocation.getText()         : "";
        String locationGuidance = txtLocationGuidance != null ? txtLocationGuidance.getText() : "";
        String notes            = txtInformation      != null ? txtInformation.getText()      : "";

        if (!startPickerController.isValid()) {
            AlertUtil.showWarning("Missing information",
                    "Please enter a valid start date and time (DD-MM-YYYY HH:MM).");
            return;
        }
        if (!endPickerController.isValid()) {
            AlertUtil.showWarning("Missing information",
                    "Please enter a valid end date and time (DD-MM-YYYY HH:MM).");
            return;
        }

        String startDateTime = startPickerController.getDateTime();
        String endDateTime   = endPickerController.getDateTime();

        try {
            if (eventToEdit == null) {
                eventManager.createEvent(name, startDateTime, endDateTime,
                        location, locationGuidance, notes);
                AlertUtil.showInfo("Event saved", "Event \"" + name + "\" has been created.");
            } else {
                eventManager.editEvent(eventToEdit.getId(), name, startDateTime, endDateTime,
                        location, locationGuidance, notes);
                AlertUtil.showInfo("Event updated", "Event \"" + name + "\" has been updated.");
            }
            SceneUtil.switchScene(actionEvent, "Views/Events.fxml");
        } catch (IllegalArgumentException e) {
            AlertUtil.showWarning("Invalid input", e.getMessage());
        } catch (Exception e) {
            AlertUtil.showError("Database error", e.getMessage());
        }
    }

    @FXML
    public void onCancelEventClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
    }
}