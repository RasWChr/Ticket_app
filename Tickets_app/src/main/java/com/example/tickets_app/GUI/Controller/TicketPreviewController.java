package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.Main;
import com.example.tickets_app.GUI.util.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.UUID;

public class TicketPreviewController {

    @FXML private VBox ticketContainer;

    public void setTicket(Ticket ticket) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Views/TicketLayout.fxml"));
            VBox ticketLayout = loader.load();
            TicketLayoutController controller = loader.getController();
            controller.setTicket(ticket);
            ticketContainer.getChildren().setAll(ticketLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onBackClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/TicketList.fxml");
    }
}