package com.example.tickets_app.GUI.Controller;


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


    public TicketListCell(Consumer<Ticket> onDelete, Consumer<Ticket> onEdit) {
        this.onDelete = onDelete;
        this.onEdit = onEdit;
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
                        Main.class.getResource("Views/TicketListCell.fxml"));
                HBox root = loader.load();
                TicketListCellController controller = loader.getController();
                controller.setTicket(onDelete, onEdit, ticket);
                setGraphic(root);
                setText(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
