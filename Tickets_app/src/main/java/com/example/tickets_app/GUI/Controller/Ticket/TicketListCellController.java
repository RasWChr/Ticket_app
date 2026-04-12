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
    @FXML private Label  lblScope;
    @FXML private Button btnPreview;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;

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

        //Scope
        if (ticket.isGlobal()) {
            lblScope.setText("✦ All Events");
            lblScope.getStyleClass().setAll("scope-badge-global");
        } else {
            String eventLabel = ticket.getEventName() != null
                    ? ticket.getEventName() : "Unknown Event";
            lblScope.setText("📅 " + eventLabel);
            lblScope.getStyleClass().setAll("scope-badge-event");
        }

        // Wire buttons directly
        btnPreview.setOnAction(e -> { if (onPreview != null) onPreview.accept(ticket); });
        btnDelete.setOnAction(e -> { if (onDelete != null) onDelete.accept(ticket); });
        btnEdit.setOnAction(e -> { if (onEdit != null) onEdit.accept(ticket); });

        // Show Edit only for Coordinators
        String role = SessionManager.getLoggedInUser().getRole();
        btnEdit.setVisible(role.equals("Coordinator"));
        btnEdit.setManaged(role.equals("Coordinator"));
    }

    private String formatDetails(Ticket ticket) {
        double price    = ticket.getPrice();
        double discount = ticket.getDiscount();

        if (discount > 0) {
            double final_ = price - (price * (discount / 100));
            return String.format("%.2f kr → %.2f kr  (%.0f%% off)", price, final_, discount);
        } else {
            return price == 0
                    ? "FREE"
                    : String.format("%.2f kr", price);
        }
    }
}