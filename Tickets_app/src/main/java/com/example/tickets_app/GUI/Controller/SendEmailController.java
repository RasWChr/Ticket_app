package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.GUI.util.EmailUtil;
import com.example.tickets_app.GUI.util.TicketPrintUtil;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class SendEmailController {

    @FXML private TextField txtEmail;
    @FXML private Label     lblStatus;
    @FXML private Button    btnSend;

    private VBox   ticketNode;
    private Ticket ticket;
    private Event  event;

    public void setData(VBox ticketNode, Ticket ticket, Event event) {
        this.ticketNode = ticketNode;
        this.ticket     = ticket;
        this.event      = event;
    }

    @FXML
    public void onSendClick(ActionEvent actionEvent) {
        String email = txtEmail.getText().trim();

        if (email.isBlank()) {
            lblStatus.setText("Please enter an email address.");
            lblStatus.setStyle("-fx-text-fill: #FF9800;");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            lblStatus.setText("Please enter a valid email address.");
            lblStatus.setStyle("-fx-text-fill: #FF9800;");
            return;
        }

        btnSend.setDisable(true);
        lblStatus.setText("Generating PDF...");
        lblStatus.setStyle("-fx-text-fill: #AAAAAA;");

        String eventName = event != null ? event.getName() : "Event";

        // Step 1 — snapshot MUST happen on the FX thread
        File pdf = TicketPrintUtil.saveAsTempPdf(ticketNode, eventName);

        if (pdf == null) {
            lblStatus.setText("Could not generate PDF.");
            lblStatus.setStyle("-fx-text-fill: #C0392B;");
            btnSend.setDisable(false);
            return;
        }

        lblStatus.setText("Sending email...");

        // Step 2 — only the network call goes to a background thread
        File finalPdf = pdf;
        Task<Void> sendTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                EmailUtil.sendTicket(email, eventName, finalPdf);
                return null;
            }
        };

        sendTask.setOnSucceeded(e -> {
            lblStatus.setText("Email sent successfully to " + email);
            lblStatus.setStyle("-fx-text-fill: #4CAF50;");
            btnSend.setDisable(false);
        });

        sendTask.setOnFailed(e -> {
            Throwable ex = sendTask.getException();
            lblStatus.setText("Failed to send: " + ex.getMessage());
            lblStatus.setStyle("-fx-text-fill: #C0392B;");
            btnSend.setDisable(false);
        });

        Thread thread = new Thread(sendTask);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    public void onCancelClick(ActionEvent actionEvent) {
        ((Stage) txtEmail.getScene().getWindow()).close();
    }
}