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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TicketPrintUtil {

    /**
     * Snapshots the given JavaFX node, embeds it into a PDF sized to fit,
     * and saves it to a location chosen by the user via a file dialog.
     */
    public static void saveAsPdf(Node ticketNode, Window ownerWindow) {
        // 1. Snapshot the ticket node to a high-res image
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.WHITE);

        // 2x scale for crisp output
        params.setTransform(javafx.scene.transform.Transform.scale(2, 2));
        WritableImage fxImage = ticketNode.snapshot(params, null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);

        // 2. Let user choose where to save
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Ticket as PDF");
        fileChooser.setInitialFileName("ticket.pdf");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File file = fileChooser.showSaveDialog(ownerWindow);

        if (file == null) return; // user cancelled

        // 3. Build the PDF
        try (PDDocument doc = new PDDocument()) {
            // Page sized to match the ticket image
            float imgWidth  = bufferedImage.getWidth();
            float imgHeight = bufferedImage.getHeight();

            // Scale to a reasonable page width (A5-ish)
            float pageWidth  = 420f;
            float pageHeight = pageWidth * (imgHeight / imgWidth);

            PDRectangle pageSize = new PDRectangle(pageWidth, pageHeight);
            PDPage page = new PDPage(pageSize);
            doc.addPage(page);

            PDImageXObject pdImage = LosslessFactory.createFromImage(doc, bufferedImage);

            try (PDPageContentStream cs = new PDPageContentStream(doc, page)) {
                cs.drawImage(pdImage, 0, 0, pageWidth, pageHeight);
            }

            doc.save(file);
            AlertUtil.showInfo("Saved", "Ticket saved to:\n" + file.getAbsolutePath());

        } catch (IOException e) {
            AlertUtil.showError("Save failed", "Could not save PDF: " + e.getMessage());
        }
    }
}