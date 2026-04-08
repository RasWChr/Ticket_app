package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.Main;
import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import com.example.tickets_app.GUI.util.TicketPrintUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class TicketPreviewController {

    @FXML private VBox ticketContainer;

    private VBox ticketNode;

    public void setTicket(Ticket ticket) {
        setTicket(ticket, null);
    }

    public void setTicket(Ticket ticket, Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Views/TicketLayout.fxml"));
            ticketNode = loader.load();
            TicketLayoutController controller = loader.getController();
            controller.setTicket(ticket, event);
            ticketContainer.getChildren().setAll(ticketNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the ticket in a standalone popup window.
     * Called from TicketListController instead of switchSceneWithController.
     */
    public static void openAsWindow(Ticket ticket, Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Views/TicketPreview.fxml"));
            VBox root = loader.load();

            TicketPreviewController controller = loader.getController();
            controller.setTicket(ticket, event);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(Main.class.getResource("styles.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Ticket Preview");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onSaveAsPdfClick(ActionEvent actionEvent) {
        if (ticketNode == null) {
            AlertUtil.showWarning("Nothing to print", "No ticket is loaded.");
            return;
        }
        TicketPrintUtil.saveAsPdf(ticketNode,
                ticketContainer.getScene().getWindow());
    }

    @FXML
    public void onBackClick(ActionEvent actionEvent) {
        // Close this window if opened as popup, otherwise navigate back
        Stage stage = (Stage) ticketContainer.getScene().getWindow();
        stage.close();
    }
}