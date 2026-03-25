package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.function.Consumer;

public class EventListCell extends ListCell<Event> {

    private final Consumer<Event> onDelete;
    private final Consumer<Event> onAssign;
    private final Consumer<Event> onInfo;
    private final Consumer<Event> onEdit;

    public EventListCell(Consumer<Event> onDelete, Consumer<Event> onAssign,
                         Consumer<Event> onInfo, Consumer<Event> onEdit) {
        this.onDelete = onDelete;
        this.onAssign = onAssign;
        this.onInfo = onInfo;
        this.onEdit = onEdit;
    }

    @Override
    protected void updateItem(Event event, boolean empty) {
        super.updateItem(event, empty);

        if (empty || event == null) {
            setText(null);
            setGraphic(null);
        } else {
            try {
                FXMLLoader loader = new FXMLLoader(
                        Main.class.getResource("Views/EventListCell.fxml"));
                HBox root = loader.load();
                EventListCellController controller = loader.getController();
                controller.setEvent(event, onDelete, onAssign, onInfo, onEdit);
                setGraphic(root);
                setText(null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}