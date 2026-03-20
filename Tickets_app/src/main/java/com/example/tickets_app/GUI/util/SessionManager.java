package com.example.tickets_app.GUI.util;

import com.example.tickets_app.BE.User;
import com.example.tickets_app.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    public static void redirectToLogin(){
        try {
            Stage stage = (Stage) Window.getWindows().stream().filter(Window::isShowing).findFirst().orElseThrow();

            Parent root = FXMLLoader.load(Main.class.getResource("Views/Log-in.fxml"));
            stage.setScene(new javafx.scene.Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
