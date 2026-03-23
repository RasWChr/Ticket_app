package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BLL.EventManager;
import com.example.tickets_app.BLL.Interface.IEventManager;
import com.example.tickets_app.DAL.DAO.EventDAO;
import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class NewEditEventController {

    @FXML private TextField txtExtra;
    @FXML private TextField txtEventName;
    @FXML private ChoiceBox<String> cBoxStartTime;
    @FXML private ChoiceBox<String> cBoxEndTime;
    @FXML private ChoiceBox<String> cBoxDay;
    @FXML private ChoiceBox<String> cBoxMonth;
    @FXML private TextField txtLocation;
    @FXML private TextField txtLocationGuidance;
    @FXML private TextField txtInformation;

    private final IEventManager eventManager = new EventManager(new EventDAO());

    @FXML
    public void initialize() {
        cBoxStartTime.getItems().addAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00");
        cBoxEndTime.getItems().addAll("08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00");
        cBoxDay.getItems().addAll("01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31");
        cBoxMonth.getItems().addAll("01","02","03","04","05","06","07","08","09","10","11","12");
    }

    @FXML
    public void onSaveEventClick(ActionEvent actionEvent) {
        String name             = txtEventName        != null ? txtEventName.getText()        : "";
        String location         = txtLocation         != null ? txtLocation.getText()         : "";
        String locationGuidance = txtLocationGuidance != null ? txtLocationGuidance.getText() : "";
        String notes            = txtInformation      != null ? txtInformation.getText()      : "";

        if (cBoxDay.getValue() == null || cBoxMonth.getValue() == null ||
                cBoxStartTime.getValue() == null || cBoxEndTime.getValue() == null) {
            AlertUtil.showWarning("Missing information", "Please select a day, month, start time and end time.");
            return;
        }

        int currentYear = java.time.Year.now().getValue();
        String date          = cBoxDay.getValue() + "-" + cBoxMonth.getValue() + "-" + currentYear;
        String startDateTime = date + " " + cBoxStartTime.getValue();
        String endDateTime   = date + " " + cBoxEndTime.getValue();

        try {
            eventManager.createEvent(name, startDateTime, endDateTime, location, locationGuidance, notes);
            AlertUtil.showInfo("Event saved", "Event \"" + name + "\" has been created.");
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