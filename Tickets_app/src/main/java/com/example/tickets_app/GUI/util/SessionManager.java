package com.example.tickets_app.GUI.util;

import com.example.tickets_app.BE.User;
import com.example.tickets_app.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class SessionManager {

    private static User loggedInUser;

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void clearSession() {
        loggedInUser = null;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public static void redirectToLogin() {
        try {
            Stage stage = (Stage) Window.getWindows()
                    .stream()
                    .filter(Window::isShowing)
                    .findFirst()
                    .orElseThrow();

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Views/Log-in.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(Main.class.getResource("styles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}