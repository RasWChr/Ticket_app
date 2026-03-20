package com.example.tickets_app.GUI.util;

import com.example.tickets_app.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class SceneUtil {

    public static void switchScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(Main.class.getResource(fxmlPath));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Gør det muligt at have switch til edit i den samme controller som controllere de andre FXML.
    public static <C> void switchSceneWithController(Node source, String fxmlPath, Consumer<C> controllerSetup) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlPath));
            Parent root = loader.load();

            C controller = loader.getController();
            controllerSetup.accept(controller);

            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}