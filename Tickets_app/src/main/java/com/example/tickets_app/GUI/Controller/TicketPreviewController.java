package com.example.tickets_app.GUI.Controller;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.Ticket;
import com.example.tickets_app.Main;
import com.example.tickets_app.GUI.util.AlertUtil;
import com.example.tickets_app.GUI.util.SceneUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;

import java.io.IOException;

public class TicketPreviewController {

    @FXML private VBox ticketContainer;

    private VBox ticketNode;

    public void setTicket(Ticket ticket) {
        setTicket(ticket, null);
    }

    public void setTicket(Ticket ticket, Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("Views/TicketLayout.fxml"));
            ticketNode = loader.load();
            TicketLayoutController controller = loader.getController();
            controller.setTicket(ticket, event);
            ticketContainer.getChildren().setAll(ticketNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onPrintClick(ActionEvent actionEvent) {
        if (ticketNode == null) {
            AlertUtil.showWarning("Nothing to print", "No ticket is loaded.");
            return;
        }

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job == null) {
            AlertUtil.showError("Print error", "No printer found on this machine.");
            return;
        }

        // Show the system print dialog
        boolean proceed = job.showPrintDialog(ticketNode.getScene().getWindow());
        if (!proceed) return;

        Printer printer = job.getPrinter();
        PageLayout pageLayout = printer.createPageLayout(
                Paper.A4, PageOrientation.PORTRAIT,
                Printer.MarginType.DEFAULT);

        // Scale the ticket to fit the printable area
        double printWidth  = pageLayout.getPrintableWidth();
        double printHeight = pageLayout.getPrintableHeight();
        double ticketWidth  = ticketNode.getBoundsInParent().getWidth();
        double ticketHeight = ticketNode.getBoundsInParent().getHeight();

        double scaleX = printWidth  / ticketWidth;
        double scaleY = printHeight / ticketHeight;
        double scale  = Math.min(scaleX, scaleY);

        Scale transform = new Scale(scale, scale);
        ticketNode.getTransforms().add(transform);

        boolean printed = job.printPage(pageLayout, ticketNode);

        // Remove the transform after printing so the UI looks normal
        ticketNode.getTransforms().remove(transform);

        if (printed) {
            job.endJob();
            AlertUtil.showInfo("Print successful", "Ticket sent to printer.");
        } else {
            AlertUtil.showError("Print failed", "The ticket could not be printed.");
        }
    }

    @FXML
    public void onBackClick(ActionEvent actionEvent) {
        SceneUtil.switchScene(actionEvent, "Views/TicketList.fxml");
    }
}