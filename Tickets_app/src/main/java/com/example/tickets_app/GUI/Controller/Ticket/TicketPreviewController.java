package com.example.tickets_app.GUI.Controller.Ticket;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.GUI.Controller.Misc.SendEmailController;
import com.example.tickets_app.Main;
import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.TicketPrintUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class TicketPreviewController {

    @FXML private VBox ticketContainer;

    private VBox   ticketNode;
    private Ticket ticket;
    private Event  event;

    public void setTicket(Ticket ticket) {
        setTicket(ticket, null);
    }

    public void setTicket(Ticket ticket, Event event) {
        this.ticket = ticket;
        this.event  = event;
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("Views/Ticket/TicketLayout.fxml"));
            ticketNode = loader.load();
            TicketLayoutController controller = loader.getController();
            controller.setTicket(ticket, event);
            ticketContainer.getChildren().setAll(ticketNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openAsWindow(Ticket ticket, Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("Views/Ticket/TicketPreview.fxml"));
            VBox root = loader.load();

            TicketPreviewController controller = loader.getController();
            controller.setTicket(ticket, event);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Main.class.getResource("styles.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Ticket — " + (event != null ? event.getName() : "Preview"));
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
            AlertUtil.showWarning("Nothing to export", "No ticket is loaded.");
            return;
        }
        File saved = TicketPrintUtil.saveAsPdf(
                ticketNode, ticketContainer.getScene().getWindow());
        if (saved != null) {
            AlertUtil.showInfo("Saved",
                    "Ticket saved to:\n" + saved.getAbsolutePath());
        }
    }

    @FXML
    public void onSendEmailClick(ActionEvent actionEvent) {
        if (ticketNode == null) {
            AlertUtil.showWarning("Nothing to send", "No ticket is loaded.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                    Main.class.getResource("Views/Misc/SendEmailDialog.fxml"));
            VBox root = loader.load();

            SendEmailController controller = loader.getController();
            controller.setData(ticketNode, ticket, event);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                    Main.class.getResource("styles.css").toExternalForm());

            Stage stage = new Stage();
            stage.setTitle("Send Ticket by Email");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onBackClick(ActionEvent actionEvent) {
        ((Stage) ticketContainer.getScene().getWindow()).close();
    }
}