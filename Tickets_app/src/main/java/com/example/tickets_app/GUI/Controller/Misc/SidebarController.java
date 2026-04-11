package com.example.tickets_app.GUI.Controller.Misc;

import com.example.tickets_app.BE.User;
import com.example.tickets_app.GUI.util.SceneUtil;
import com.example.tickets_app.GUI.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SidebarController {

    @FXML private Label lblSidebarName;
    @FXML private Label lblSidebarRole;

    // Role-gated buttons
    @FXML private Button btnSidebarEvents;
    @FXML private Button btnSidebarCreateEvent;
    @FXML private Button btnSidebarTicketList;
    @FXML private Button btnSidebarPrintTicket;
    @FXML private Button btnSidebarCreateUser;
    @FXML private Button btnSidebarManageUsers;
    @FXML private Label  lblAdminSection;

    @FXML
    public void initialize() {
        if (!SessionManager.isLoggedIn()) return;

        User user = SessionManager.getLoggedInUser();
        lblSidebarName.setText(user.getFirstName() + " " + user.getLastName());
        lblSidebarRole.setText(user.getRole().toUpperCase());

        String role = user.getRole();

        if (role.equals("Admin")) {
            // Admin: see events, user management — no create event/ticket
            setVisible(btnSidebarCreateEvent, false);
            setVisible(btnSidebarTicketList, false);
            setVisible(btnSidebarPrintTicket, false);
            setVisible(btnSidebarCreateUser, true);
            setVisible(btnSidebarManageUsers, true);
            setVisible(lblAdminSection, true);
        } else if (role.equals("Coordinator")) {
            // Coordinator: events, create event, tickets — no user management
            setVisible(btnSidebarCreateEvent, true);
            setVisible(btnSidebarTicketList, true);
            setVisible(btnSidebarPrintTicket, true);
            setVisible(btnSidebarCreateUser, false);
            setVisible(btnSidebarManageUsers, false);
            setVisible(lblAdminSection, false);
        }
    }

    private void setVisible(javafx.scene.Node node, boolean visible) {
        node.setVisible(visible);
        node.setManaged(visible);
    }

    /** Call this from host controllers to highlight the active page button. */
    public void setActivePage(String page) {
        // Clear all active styles first
        for (Button btn : new Button[]{
                btnSidebarCreateEvent, btnSidebarTicketList,
                btnSidebarPrintTicket, btnSidebarCreateUser, btnSidebarManageUsers}) {
            if (btn != null) btn.getStyleClass().remove("sidebar-nav-btn-active");
        }

        // Find the matching button and add active style
        switch (page) {
            case "events"        -> addActive(btnSidebarCreateEvent);  // handled by events btn below
            case "createEvent"   -> addActive(btnSidebarCreateEvent);
            case "ticketList"    -> addActive(btnSidebarTicketList);
            case "printTicket"   -> addActive(btnSidebarPrintTicket);
            case "createUser"    -> addActive(btnSidebarCreateUser);
            case "manageUsers"   -> addActive(btnSidebarManageUsers);
        }
    }

    private void addActive(Button btn) {
        if (btn != null && !btn.getStyleClass().contains("sidebar-nav-btn-active")) {
            btn.getStyleClass().add("sidebar-nav-btn-active");
        }
    }

    @FXML public void onEventsClick(ActionEvent e) {
        SceneUtil.switchScene(e, "Views/Event/Events.fxml");
    }
    @FXML public void onCreateEventClick(ActionEvent e) {
        SceneUtil.switchScene(e, "Views/Event/New-Edit-Events.fxml");
    }
    @FXML public void onTicketListClick(ActionEvent e) {
        SceneUtil.switchScene(e, "Views/Ticket/TicketList.fxml");
    }
    @FXML public void onPrintTicketClick(ActionEvent e) {
        SceneUtil.switchScene(e, "Views/Ticket/Tickets.fxml");
    }
    @FXML public void onCreateUserClick(ActionEvent e) {
        SceneUtil.switchScene(e, "Views/User/Create-Edit-Users.fxml");
    }
    @FXML public void onManageUsersClick(ActionEvent e) {
        SceneUtil.switchScene(e, "Views/User/Users.fxml");
    }
    @FXML public void onLogOutClick(ActionEvent e) {
        SessionManager.clearSession();
        SceneUtil.switchScene(e, "Views/Misc/Log-in.fxml");
    }

}