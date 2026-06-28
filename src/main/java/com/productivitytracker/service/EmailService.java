package com.productivitytracker.service;

import com.productivitytracker.config.AppConfig;
import com.productivitytracker.model.User;
import com.productivitytracker.util.Logger;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {

    private final boolean enabled = AppConfig.getBoolean("mail.enabled", false);
    private final String baseUrl = AppConfig.get("app.baseUrl", "http://localhost:8080/ProductivityTracker");

    public void sendPasswordResetEmail(User user, String token) {
        String link = baseUrl + "/ResetPasswordServlet?token=" + token;
        send(user.getEmail(), "Reset your Productivity Tracker password",
                "Use this link to reset your password. The link expires in 60 minutes.\n\n" + link);
    }

    public void sendEmailVerification(User user, String token) {
        String link = baseUrl + "/EmailVerificationServlet?token=" + token;
        send(user.getEmail(), "Verify your Productivity Tracker email",
                "Verify your email address with this link. It expires in 24 hours.\n\n" + link);
    }

    public void sendReminderEmail(User user, String title) {
        send(user.getEmail(), "Reminder: " + title,
                "Reminder from Productivity Tracker:\n\n" + title + "\n\nOpen the app to review your tasks and habits.");
    }

    public void send(String to, String subject, String body) {
        if (!enabled) {
            Logger.logInfo("Email disabled. Would send to " + to + " [" + subject + "]: " + body);
            return;
        }

        try {
            Transport.send(buildMessage(to, subject, body));
        } catch (MessagingException e) {
            Logger.logError("Failed to send email", e);
        }
    }

    private Message buildMessage(String to, String subject, String body) throws MessagingException {
        String username = AppConfig.get("mail.username", "");
        String password = AppConfig.get("mail.password", "");
        Properties props = new Properties();
        props.put("mail.smtp.auth", String.valueOf(!username.isBlank()));
        props.put("mail.smtp.starttls.enable", AppConfig.get("mail.starttls", "true"));
        props.put("mail.smtp.host", AppConfig.get("mail.host", "localhost"));
        props.put("mail.smtp.port", AppConfig.get("mail.port", "587"));

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                if (username.isBlank()) {
                    return null;
                }
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(AppConfig.get("mail.from", "no-reply@productivitytracker.local")));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);
        return message;
    }
}
