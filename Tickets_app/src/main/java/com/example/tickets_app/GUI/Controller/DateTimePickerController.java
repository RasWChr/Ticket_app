package com.example.tickets_app.GUI.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimePickerController {

    @FXML private DatePicker datePicker;
    @FXML private TextField txtTime;

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @FXML
    public void initialize() {
        // Format the DatePicker to show dd-MM-yyyy
        datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? DATE_FORMATTER.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string == null || string.isBlank()) return null;
                try {
                    return LocalDate.parse(string, DATE_FORMATTER);
                } catch (Exception e) {
                    return null;
                }
            }
        });

        // Auto-format time input: insert colon after 2 digits
        txtTime.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) return;

            // Strip non-digits
            String digits = newVal.replaceAll("[^0-9]", "");

            if (digits.length() > 4) digits = digits.substring(0, 4);

            String formatted;
            if (digits.length() <= 2) {
                formatted = digits;
            } else {
                formatted = digits.substring(0, 2) + ":" + digits.substring(2);
            }

            if (!formatted.equals(newVal)) {
                txtTime.setText(formatted);
                txtTime.positionCaret(formatted.length());
            }
        });
    }

    /**
     * Returns the combined datetime string in format dd-MM-yyyy HH:mm
     * or null if input is incomplete/invalid.
     */
    public String getDateTime() {
        LocalDate date = datePicker.getValue();
        String time = txtTime.getText();

        if (date == null || time == null || !time.matches("^\\d{2}:\\d{2}$")) {
            return null;
        }

        return DATE_FORMATTER.format(date) + " " + time;
    }

    /**
     * Pre-fills the picker from a stored datetime string (dd-MM-yyyy HH:mm)
     */
    public void setDateTime(String dateTime) {
        if (dateTime == null || dateTime.length() != 16) return;
        try {
            LocalDate date = LocalDate.parse(dateTime.substring(0, 10), DATE_FORMATTER);
            datePicker.setValue(date);
            txtTime.setText(dateTime.substring(11, 16));
        } catch (Exception e) {
            // leave blank if parsing fails
        }
    }

    public boolean isValid() {
        return getDateTime() != null;
    }
}