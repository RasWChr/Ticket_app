package com.example.tickets_app.GUI.util;

import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PasswordToggleUtil {

    public static boolean toggle(boolean currentlyVisible, PasswordField passwordField, TextField visibleField, Button toggleButton) {
        if (!currentlyVisible) {
            visibleField.setText(passwordField.getText());
            visibleField.setManaged(true);
            visibleField.setVisible(true);
            passwordField.setManaged(false);
            passwordField.setVisible(false);
            toggleButton.setText("-_-");
            return true;
        } else {
            passwordField.setText(visibleField.getText());
            passwordField.setManaged(true);
            passwordField.setVisible(true);
            visibleField.setManaged(false);
            visibleField.setVisible(false);
            toggleButton.setText("⊙_⊙");
            return false;
        }
    }

    public static String getPassword(boolean currentlyVisible, PasswordField passwordField, TextField visibleField) {
        return currentlyVisible ? visibleField.getText() : passwordField.getText();
    }
}