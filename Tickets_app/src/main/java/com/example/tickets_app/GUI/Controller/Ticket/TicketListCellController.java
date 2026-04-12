package com.example.tickets_app.GUI.Controller.Ticket;

import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.GUI.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.function.Consumer;

public class TicketListCellController {

    @FXML private Label  lblTicketName;
    @FXML private Label  lblTicketDetails;
    @FXML private Label  lblScope;       // shows "All Events" or event name
    @FXML private Button btnOptions;

    private Consumer<Ticket> onDelete;
    private Consumer<Ticket> onEdit;
    private Consumer<Ticket> onPreview;
    private Ticket ticket;

    public void setTicket(Consumer<Ticket> onDelete,
                          Consumer<Ticket> onEdit,
                          Consumer<Ticket> onPreview,
                          Ticket ticket) {
        this.ticket    = ticket;
        this.onDelete  = onDelete;
        this.onEdit    = onEdit;
        this.onPreview = onPreview;

        lblTicketName.setText(ticket.getTicketType());
        lblTicketDetails.setText(formatDetails(ticket));

        // Scope badge
        if (ticket.isGlobal()) {
            lblScope.setText("✦ All Events");
            lblScope.getStyleClass().setAll("scope-badge-global");
        } else {
            String eventLabel = ticket.getEventName() != null
                    ? ticket.getEventName() : "Unknown Event";
            lblScope.setText("📅 " + eventLabel);
            lblScope.getStyleClass().setAll("scope-badge-event");
        }

        buildContextMenu();
    }

    private String formatDetails(Ticket ticket) {
        double price    = ticket.getPrice();
        double discount = ticket.getDiscount();
        String type     = ticket.getTicketType();

        if (discount > 0) {
            double final_ = price - (price * (discount / 100));
            return String.format("%.2f kr → %.2f kr  (%.0f%% off)", price, final_, discount);
        } else {
            return price == 0
                    ? "FREE"
                    : String.format("%.2f kr", price);
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

        menu.getItems().addAll(itemPreview, itemDelete);

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

    @FXML public void onOptionsClick(ActionEvent actionEvent) { }
}