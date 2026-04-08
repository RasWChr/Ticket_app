package com.example.tickets_app.GUI.Controller.Ticket;


import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.function.Consumer;


public class TicketListCell extends ListCell<Ticket> {

    private final Consumer<Ticket> onDelete;
    private final Consumer<Ticket> onEdit;
    private final Consumer<Ticket> onPreview;



    public TicketListCell(Consumer<Ticket> onDelete, Consumer<Ticket> onEdit, Consumer<Ticket> onPreview) {
        this.onDelete = onDelete;
        this.onEdit = onEdit;
        this.onPreview = onPreview;
    }



    @Override
    protected void updateItem(Ticket ticket, boolean empty) {
        super.updateItem(ticket, empty);

        if (empty || ticket == null) {
            setText(null);
            setGraphic(null);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(
                        Main.class.getResource("Views/Ticket/TicketListCell.fxml"));
                HBox root = loader.load();
                TicketListCellController controller = loader.getController();
                controller.setTicket(onDelete, onEdit, onPreview, ticket);
                setGraphic(root);
                setText(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
