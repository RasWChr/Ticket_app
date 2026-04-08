package com.example.tickets_app.GUI.util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TicketPrintUtil {

    /**
     * Snapshots the ticket node and saves it as a PDF chosen by the user.
     * Returns the saved File so it can be reused for email attachment.
     */
    public static File saveAsPdf(Node ticketNode, Window ownerWindow) {
        BufferedImage bufferedImage = snapshot(ticketNode);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Ticket as PDF");
        fileChooser.setInitialFileName("ticket.pdf");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(ownerWindow);

        if (file == null) return null;

        return writePdf(bufferedImage, file);
    }

    /**
     * Saves directly to a temp file without a dialog — used for email.
     */
    public static File saveAsTempPdf(Node ticketNode, String eventName) {
        BufferedImage bufferedImage = snapshot(ticketNode);
        try {
            File temp = File.createTempFile(
                    "ticket-" + eventName.replaceAll("\\s+", "_"), ".pdf");
            temp.deleteOnExit();
            return writePdf(bufferedImage, temp);
        } catch (IOException e) {
            AlertUtil.showError("Error", "Could not create temporary file: " + e.getMessage());
            return null;
        }
    }

    private static BufferedImage snapshot(Node ticketNode) {
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.WHITE);
        params.setTransform(javafx.scene.transform.Transform.scale(2, 2));
        WritableImage fxImage = ticketNode.snapshot(params, null);
        return SwingFXUtils.fromFXImage(fxImage, null);
    }

    private static File writePdf(BufferedImage image, File file) {
        float pageWidth  = 420f;
        float pageHeight = pageWidth * ((float) image.getHeight() / image.getWidth());

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(new PDRectangle(pageWidth, pageHeight));
            doc.addPage(page);

            PDImageXObject pdImage = LosslessFactory.createFromImage(doc, image);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.drawImage(pdImage, 0, 0, pageWidth, pageHeight);
            }

            doc.save(file);
            return file;

        } catch (IOException e) {
            AlertUtil.showError("Save failed", "Could not save PDF: " + e.getMessage());
            return null;
        }
    }
}