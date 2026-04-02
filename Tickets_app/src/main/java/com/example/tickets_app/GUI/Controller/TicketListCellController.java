package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.GUI.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.function.Consumer;

public class TicketListCellController {

    @FXML private Label lblTicketName;
    @FXML private Label lblTicketDetails;
    @FXML private Button btnOptions;

    private String eventName;

    private Consumer<Ticket> onDelete;
    private Consumer<Ticket> onEdit;
    private Ticket ticket;

    public void setTicket(Consumer<Ticket> onDelete,
                          Consumer<Ticket> onEdit,
                          Ticket ticket) {
        this.ticket = ticket;
        this.onDelete = onDelete;
        this.onEdit = onEdit;

        lblTicketName.setText(ticket.getEventName());
        lblTicketDetails.setText(ticket.getPrice() + " | " + ticket.getTicketType());

        buildContextMenu();
    }

    private void buildContextMenu() {
        ContextMenu menu = new ContextMenu();
        menu.getStyleClass().add("dark-context-menu");

        String role = SessionManager.getLoggedInUser().getRole();

        MenuItem itemDelete = new MenuItem("🗑 Delete");
        itemDelete.setOnAction(e -> { if (onDelete != null) onDelete.accept(ticket); });

        menu.getItems().addAll(itemDelete);

        // Coordinator-only options
        if (role.equals("Coordinator")) {
            menu.getItems().add(new SeparatorMenuItem());

            MenuItem itemEdit = new MenuItem("✏ Edit");
            itemEdit.setOnAction(e -> { if (onEdit != null) onEdit.accept(ticket); });

            menu.getItems().add(itemEdit);
        }


        btnOptions.setOnAction(e ->
                menu.show(btnOptions,
                        btnOptions.localToScreen(0, 0).getX(),
                        btnOptions.localToScreen(0, 0).getY() + btnOptions.getHeight()));
    }


    public void onOptionsClick(ActionEvent actionEvent) {
    }
}
