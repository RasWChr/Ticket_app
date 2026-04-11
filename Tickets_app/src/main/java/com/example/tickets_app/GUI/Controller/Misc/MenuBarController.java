package com.example.tickets_app.GUI.Controller.Misc;

import com.example.tickets_app.BE.User;
import com.example.tickets_app.GUI.util.SceneUtil;
import com.example.tickets_app.GUI.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MenuBarController {

    @FXML private Button btnHamburger;
    @FXML private Label  lblPageTitle;
    @FXML private Label  lblTopUser;
    @FXML private VBox   menuBarRoot;
    @FXML private Button btnBack;


    // The overlay panel — built once and toggled
    private VBox menuOverlay;
    private StackPane hostStack; // set by host controller after fx:include
    private boolean menuOpen = false;


    @FXML
    public void initialize() {
        if (!SessionManager.isLoggedIn()) return;

        User user = SessionManager.getLoggedInUser();
        lblTopUser.setText(user.getFirstName() + " (" + user.getRole() + ")");
    }

    /** Host controller calls this to give the menu bar a reference to the StackPane root. */
    public void setup(StackPane hostStack, String pageTitle) {
        this.hostStack = hostStack;
        lblPageTitle.setText(pageTitle);
        buildOverlay();
    }

    public void setPageTitle(String title) {
        lblPageTitle.setText(title);
    }

    @FXML
    public void onHamburgerClick(ActionEvent e) {
        if (hostStack == null) return;

        if (menuOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    private void openMenu() {
        if (!hostStack.getChildren().contains(menuOverlay)) {
            hostStack.getChildren().add(menuOverlay);
        }
        menuOverlay.setVisible(true);
        menuOpen = true;
        btnHamburger.setText("✕");
    }

    private void closeMenu() {
        menuOverlay.setVisible(false);
        menuOpen = false;
        btnHamburger.setText("☰");
    }

    private void buildOverlay() {
        menuOverlay = new VBox(2);
        menuOverlay.setVisible(false);
        menuOverlay.getStyleClass().add("menu-overlay");
        menuOverlay.setMaxWidth(220);
        menuOverlay.setMaxHeight(Region.USE_PREF_SIZE);
        menuOverlay.setPadding(new Insets(12, 12, 16, 12));

        StackPane.setAlignment(menuOverlay, javafx.geometry.Pos.TOP_LEFT);
        StackPane.setMargin(menuOverlay, new Insets(48, 0, 0, 0)); // below top bar

        User user = SessionManager.getLoggedInUser();
        String role = user != null ? user.getRole() : "";

        // User info at the top
        Label lblName = new Label(user != null ? user.getFirstName() + " " + user.getLastName() : "");
        lblName.getStyleClass().add("menu-overlay-username");
        Label lblRole = new Label(role.toUpperCase());
        lblRole.getStyleClass().add("menu-overlay-role");

        menuOverlay.getChildren().addAll(lblName, lblRole, makeSep());
        menuOverlay.getChildren().add(sectionLabel("NAVIGATE"));

        // Events - all roles
        menuOverlay.getChildren().add(navBtn("📅  Events", ev -> {
            closeMenu();
            SceneUtil.switchScene(ev, "Views/Event/Events.fxml");
        }));

        if (role.equals("Coordinator")) {
            menuOverlay.getChildren().add(navBtn("➕  Create Event", ev -> {
                closeMenu();
                SceneUtil.switchScene(ev, "Views/Event/New-Edit-Events.fxml");
            }));
            menuOverlay.getChildren().add(navBtn("🎫  Ticket List", ev -> {
                closeMenu();
                SceneUtil.switchScene(ev, "Views/Ticket/TicketList.fxml");
            }));
            menuOverlay.getChildren().add(navBtn("🖨  Print Ticket", ev -> {
                closeMenu();
                SceneUtil.switchScene(ev, "Views/Ticket/Tickets.fxml");
            }));
        }

        if (role.equals("Admin")) {
            menuOverlay.getChildren().add(makeSep());
            menuOverlay.getChildren().add(sectionLabel("ADMIN"));
            menuOverlay.getChildren().add(navBtn("👤  Create User", ev -> {
                closeMenu();
                SceneUtil.switchScene(ev, "Views/User/Create-Edit-Users.fxml");
            }));
            menuOverlay.getChildren().add(navBtn("👥  Manage Users", ev -> {
                closeMenu();
                SceneUtil.switchScene(ev, "Views/User/Users.fxml");
            }));
        }

        // Spacer then logout
        menuOverlay.getChildren().add(makeSep());
        Button logoutBtn = navBtn("⏻  Log Out", ev -> {
            closeMenu();
            SessionManager.clearSession();
            SceneUtil.switchScene(ev, "Views/Misc/Log-in.fxml");
        });
        logoutBtn.getStyleClass().add("menu-overlay-logout");
        menuOverlay.getChildren().add(logoutBtn);
    }

    private Button navBtn(String text, javafx.event.EventHandler<ActionEvent> handler) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.getStyleClass().add("menu-overlay-btn");
        btn.setOnAction(handler);
        return btn;
    }

    private Label sectionLabel(String text) {
        Label lbl = new Label(text);
        lbl.getStyleClass().add("menu-overlay-section");
        return lbl;
    }

    private Separator makeSep() {
        Separator sep = new Separator();
        sep.getStyleClass().add("menu-overlay-sep");
        VBox.setMargin(sep, new Insets(6, 0, 6, 0));
        return sep;
    }

    public void onBackClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/Main-Screen.fxml");
    }
    public void hideBackButton() {
        if (btnBack != null) {
            btnBack.setVisible(false);
            btnBack.setManaged(false);
        }
    }

}