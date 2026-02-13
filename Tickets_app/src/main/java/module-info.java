module com.example.tickets_app {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tickets_app to javafx.fxml;
    exports com.example.tickets_app;
    exports com.example.tickets_app.GUI;
    opens com.example.tickets_app.GUI to javafx.fxml;
    exports com.example.tickets_app.GUI.Controller;
    opens com.example.tickets_app.GUI.Controller to javafx.fxml;
}