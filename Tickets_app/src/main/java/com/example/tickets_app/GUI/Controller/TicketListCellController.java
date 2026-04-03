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
    private Consumer<Ticket> onPreview;
    private Ticket ticket;

    public void setTicket(Consumer<Ticket> onDelete,
                          Consumer<Ticket> onEdit,
                          Consumer<Ticket> onPreview,
                          Ticket ticket) {
        this.ticket = ticket;
        this.onDelete = onDelete;
        this.onEdit = onEdit;
        this.onPreview = onPreview;


        lblTicketName.setText(ticket.getEventName());
        lblTicketDetails.setText(formatDetails(ticket));

        buildContextMenu();
    }

    private String formatDetails(Ticket ticket) {
        double price = ticket.getPrice();
        double discount = ticket.getDiscount();
        String type = ticket.getTicketType();

        if (discount > 0) {
            double discountedPrice = price - (price * (discount / 100));
            return String.format("%.2f → %.2f kr (%.0f%% off)", price, discountedPrice, discount);
        } else {
            return String.format("%.2f kr | %s", price, type);
        }
    }

    private void buildContextMenu() {
        ContextMenu menu = new ContextMenu();
        menu.getStyleClass().add("dark-context-menu");

        String role = SessionManager.getLoggedInUser().getRole();

        MenuItem itemPreview = new MenuItem("👁 Preview");
        itemPreview.setOnAction(e -> { if (onPreview != null) onPreview.accept(ticket); });

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
