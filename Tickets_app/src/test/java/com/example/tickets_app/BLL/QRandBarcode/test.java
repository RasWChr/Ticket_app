package com.example.tickets_app.BLL.QRandBarcode;

import com.example.tickets_app.BE.Ticket;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class test {

    // ─────────────────────────────────────────────
    // UUID Tests
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Generated UUID is not null or blank")
    void testUUIDIsNotNullOrBlank() {
        String uuid = UUID.randomUUID().toString();
        assertNotNull(uuid);
        assertFalse(uuid.isBlank());
    }

    @Test
    @DisplayName("Generated UUID follows standard format (8-4-4-4-12)")
    void testUUIDFormat() {
        String uuid = UUID.randomUUID().toString();
        assertTrue(uuid.matches(
                        "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"),
                "UUID does not match expected format: " + uuid);
    }

    @Test
    @DisplayName("1000 generated UUIDs are all unique")
    void testUUIDUniqueness() {
        Set<String> uuids = new HashSet<>();
        int count = 1000;
        for (int i = 0; i < count; i++) {
            String uuid = UUID.randomUUID().toString();
            assertTrue(uuids.add(uuid),
                    "Duplicate UUID found after " + i + " generations: " + uuid);
        }
        assertEquals(count, uuids.size());
    }

    @Test
    @DisplayName("UUID stored on Ticket is retrievable")
    void testUUIDStoredOnTicket() {
        Ticket ticket = new Ticket(1, 100.0, 0.0, "VIP");
        String uuid = UUID.randomUUID().toString();
        ticket.setUuid(uuid);
        assertEquals(uuid, ticket.getUuid());
    }

    @Test
    @DisplayName("Two tickets never share the same UUID")
    void testTwoTicketsHaveDifferentUUIDs() {
        Ticket t1 = new Ticket(1, 100.0, 0.0, "VIP");
        Ticket t2 = new Ticket(2, 200.0, 10.0, "Normal");
        t1.setUuid(UUID.randomUUID().toString());
        t2.setUuid(UUID.randomUUID().toString());
        assertNotEquals(t1.getUuid(), t2.getUuid());
    }

    @Test
    @DisplayName("UUID length is always 36 characters")
    void testUUIDLength() {
        for (int i = 0; i < 100; i++) {
            String uuid = UUID.randomUUID().toString();
            assertEquals(36, uuid.length(),
                    "UUID length was not 36: " + uuid);
        }
    }

    // ─────────────────────────────────────────────
    // Barcode (CODE_128) Tests
    // ─────────────────────────────────────────────

    @Test
    @DisplayName("Barcode generates without throwing exception")
    void testBarcodeGeneratesSuccessfully() {
        assertDoesNotThrow(() -> {
            generateBarcode(UUID.randomUUID().toString(), 400, 80);
        });
    }

    @Test
    @DisplayName("Barcode output is not empty")
    void testBarcodeOutputIsNotEmpty() throws WriterException, IOException {
        byte[] barcode = generateBarcode(UUID.randomUUID().toString(), 400, 80);
        assertNotNull(barcode);
        assertTrue(barcode.length > 0,
                "Barcode byte array should not be empty");
    }

    @Test
    @DisplayName("Barcode generates for a valid UUID string")
    void testBarcodeGeneratesForUUID() throws WriterException, IOException {
        String uuid = UUID.randomUUID().toString();
        byte[] barcode = generateBarcode(uuid, 400, 80);
        assertNotNull(barcode);
        assertTrue(barcode.length > 100,
                "Barcode output too small — likely failed to render");
    }

    @Test
    @DisplayName("QR code generates without throwing exception")
    void testQRCodeGeneratesSuccessfully() {
        assertDoesNotThrow(() -> {
            generateQRCode("TICKET-UUID: " + UUID.randomUUID(), 200, 200);
        });
    }

    @Test
    @DisplayName("QR code output is not empty")
    void testQRCodeOutputIsNotEmpty() throws WriterException, IOException {
        byte[] qr = generateQRCode("TICKET-UUID: " + UUID.randomUUID(), 200, 200);
        assertNotNull(qr);
        assertTrue(qr.length > 0,
                "QR code byte array should not be empty");
    }

    @Test
    @DisplayName("QR code encodes full ticket info without exception")
    void testQRCodeEncodesFullTicketInfo() {
        Ticket ticket = new Ticket(1, 250.0, 10.0, "VIP");
        ticket.setUuid(UUID.randomUUID().toString());
        ticket.setEventName("Disco Night");

        String content = "UUID: " + ticket.getUuid() + "\n"
                + "EVENT: " + ticket.getEventName() + "\n"
                + "TYPE: " + ticket.getTicketType() + "\n"
                + "PRICE: " + ticket.getPrice() + " kr\n";

        assertDoesNotThrow(() -> generateQRCode(content, 200, 200));
    }

    // ─────────────────────────────────────────────
    // Helpers (same logic as TicketLayoutController)
    // ─────────────────────────────────────────────

    private byte[] generateBarcode(String content, int width, int height)
            throws WriterException, IOException {
        Code128Writer writer = new Code128Writer();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.CODE_128, width, height);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        return out.toByteArray();
    }

    private byte[] generateQRCode(String content, int width, int height)
            throws WriterException, IOException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        return out.toByteArray();
    }
}