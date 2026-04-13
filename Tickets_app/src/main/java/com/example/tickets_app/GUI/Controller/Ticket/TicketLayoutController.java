package com.example.tickets_app.GUI.Controller.Ticket;

import com.example.tickets_app.BE.Event;
import com.example.tickets_app.BE.Ticket;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TicketLayoutController {

    @FXML private Label lblEventTitle;
    @FXML private Label lblTicketType;
    @FXML private Label lblDate;
    @FXML private Label lblLocation;
    @FXML private Label lblLocationGuidance;
    @FXML private Label lblCustomerName;
    @FXML private Label lblCustomerEmail;
    @FXML private Label lblPrice;
    @FXML private Label lblUuid;
    @FXML private Label lblScope;
    @FXML private ImageView imgQrCode;
    @FXML private ImageView imgBarcode;
    @FXML private VBox customerSection;

    /**
     * @param issuedUUID  The CustomerTicket-specific UUID.
     *                    If null, falls back to the Ticket template UUID.
     *                    This is what the QR code and barcode encode — unique per issued ticket.
     */
    public void setTicket(Ticket ticket, Event event,
                          String customerName, String customerEmail,
                          boolean isGlobal, String issuedUUID) {

        // Title
        lblEventTitle.setText(isGlobal
                ? "🎟 Multi-Event Ticket"
                : (event != null ? event.getName() : ticket.getEventName()));

        lblTicketType.setText(ticket.getTicketType() != null
                ? ticket.getTicketType().toUpperCase() : "");

        // Scope badge
        if (isGlobal) {
            lblScope.setText("✦ VALID FOR ALL EVENTS");
            lblScope.getStyleClass().setAll("ticket-scope-global");
        } else if (event != null) {
            lblScope.setText("📅 " + event.getName());
            lblScope.getStyleClass().setAll("ticket-scope-event");
        } else {
            lblScope.setText("");
        }

        // Date / Location
        if (!isGlobal && event != null) {
            lblDate.setText(event.getStartDateTime()
                    + (event.getEndDateTime() != null && !event.getEndDateTime().isBlank()
                    ? "  →  " + event.getEndDateTime() : ""));
            lblLocation.setText(event.getLocation());
            lblLocationGuidance.setText(
                    event.getLocationGuidance() != null && !event.getLocationGuidance().isBlank()
                            ? event.getLocationGuidance() : "");
        } else if (isGlobal) {
            lblDate.setText("Valid for any event");
            lblLocation.setText("Present at event entrance");
            lblLocationGuidance.setText("");
        } else {
            lblDate.setText("—");
            lblLocation.setText("—");
            lblLocationGuidance.setText("");
        }

        // Price
        double price    = ticket.getPrice();
        double discount = ticket.getDiscount();
        if (discount > 0) {
            double finalPrice = price - (price * (discount / 100));
            lblPrice.setText(String.format("%.2f kr  (%.0f%% off)", finalPrice, discount));
        } else {
            lblPrice.setText(price == 0 ? "FREE" : String.format("%.2f kr", price));
        }

        // The barcode/QR encodes the issued UUID (unique per customer ticket).
        // Falls back to template UUID if no issued UUID available (preview mode).
        String barcodeContent = issuedUUID != null ? issuedUUID
                : (ticket.getUuid() != null ? ticket.getUuid() : "TKT-" + ticket.getId());

        lblUuid.setText("ID: " + barcodeContent.substring(0, Math.min(8, barcodeContent.length())).toUpperCase());

        // QR code: encodes full ticket info + UUID
        try {
            imgQrCode.setImage(generateQRCode(
                    buildQrContent(ticket, event, isGlobal, barcodeContent), 200, 200));
        } catch (Exception e) { e.printStackTrace(); }

        // Barcode: encodes UUID only (for quick scanning)
        try {
            imgBarcode.setImage(generateBarcode(barcodeContent, 400, 80));
        } catch (Exception e) { e.printStackTrace(); }

        // Customer info
        lblCustomerName.setText(customerName != null ? customerName : "");
        lblCustomerEmail.setText(customerEmail != null ? customerEmail : "");

        if (customerSection != null) {
            boolean hasCustomer = customerName != null && !customerName.isBlank();
            customerSection.setVisible(hasCustomer);
            customerSection.setManaged(hasCustomer);
        }
    }

    // Backwards-compat: no issuedUUID.
    public void setTicket(Ticket ticket, Event event,
                          String customerName, String customerEmail,
                          boolean isGlobal) {
        setTicket(ticket, event, customerName, customerEmail, isGlobal, null);
    }

    // Backwards-compat: no isGlobal, no issuedUUID.
    public void setTicket(Ticket ticket, Event event,
                          String customerName, String customerEmail) {
        setTicket(ticket, event, customerName, customerEmail, false, null);
    }

    private String buildQrContent(Ticket ticket, Event event,
                                  boolean isGlobal, String uuid) {
        StringBuilder sb = new StringBuilder();
        sb.append("UUID: ").append(uuid).append("\n");
        sb.append("TICKET ID: ").append(ticket.getId()).append("\n");

        if (isGlobal) {
            sb.append("SCOPE: ALL EVENTS\n");
        } else {
            sb.append("EVENT: ")
                    .append(event != null ? event.getName() : ticket.getEventName())
                    .append("\n");
        }

        sb.append("TYPE: ").append(ticket.getTicketType()).append("\n");

        double price    = ticket.getPrice();
        double discount = ticket.getDiscount();
        if (discount > 0) {
            double finalPrice = price - (price * (discount / 100));
            sb.append("PRICE: ").append(String.format("%.2f kr (%.0f%% off)", finalPrice, discount)).append("\n");
        } else {
            sb.append("PRICE: ").append(price == 0 ? "FREE" : String.format("%.2f kr", price)).append("\n");
        }

        if (!isGlobal && event != null) {
            sb.append("DATE: ").append(event.getStartDateTime()).append("\n");
            sb.append("LOCATION: ").append(event.getLocation()).append("\n");
        }

        return sb.toString();
    }

    private Image generateQRCode(String content, int width, int height)
            throws WriterException, IOException {
        BitMatrix matrix = new QRCodeWriter()
                .encode(content, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        return new Image(new ByteArrayInputStream(out.toByteArray()));
    }

    private Image generateBarcode(String content, int width, int height)
            throws WriterException, IOException {
        BitMatrix matrix = new Code128Writer()
                .encode(content, BarcodeFormat.CODE_128, width, height);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        return new Image(new ByteArrayInputStream(out.toByteArray()));
    }
}