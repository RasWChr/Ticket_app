package com.example.tickets_app.GUI.util;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class EmailUtil {

    // Change these to your team's email credentials
    private static final String SMTP_HOST     = "smtp.gmail.com";
    private static final String SMTP_PORT     = "587";
    private static final String FROM_EMAIL    = "stejlgaardsparken40@gmail.com";
    private static final String FROM_PASSWORD = "cerr bmfr offq xpbo";

    /**
     * Sends a ticket PDF as an email attachment.
     * @param toEmail    recipient email address
     * @param eventName  used in the subject line
     * @param pdfFile    the PDF file to attach
     * @throws MessagingException if sending fails
     */
    public static void sendTicket(String toEmail, String eventName, File pdfFile)
            throws MessagingException, IOException {

        Properties props = new Properties();
        props.put("mail.smtp.auth",            "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host",            SMTP_HOST);
        props.put("mail.smtp.port",            SMTP_PORT);
        props.put("mail.smtp.ssl.trust",       SMTP_HOST);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, FROM_PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(toEmail));
        message.setSubject("Your ticket for: " + eventName);

        // Body text
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setText(
                "Hello,\n\n" +
                        "Please find your ticket for \"" + eventName + "\" attached.\n\n" +
                        "Best regards,\nEASV Bar"
        );

        // PDF attachment
        MimeBodyPart attachmentPart = new MimeBodyPart();
        attachmentPart.attachFile(pdfFile);
        attachmentPart.setFileName("ticket-" + eventName.replaceAll("\\s+", "_") + ".pdf");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);
        multipart.addBodyPart(attachmentPart);
        message.setContent(multipart);

        Transport.send(message);
    }
}