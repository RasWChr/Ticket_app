package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.BLL.Interface.ITicketManager;
import com.example.tickets_app.BLL.TicketManager;
import com.example.tickets_app.DAL.DAO.EventDAO;
import com.example.tickets_app.DAL.DAO.TicketDAO;
import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.List;

public class TicketCreatorController {
    @FXML private TextField txtPrice;
    @FXML private ChoiceBox cBoxEvent;
    @FXML private TextField txtDiscount;
    @FXML private TextField txtType;


    public void onCreateTicketClick(ActionEvent actionEvent) {
    }

    public void onBtnCancelClick(ActionEvent actionEvent) { SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
    }
}
