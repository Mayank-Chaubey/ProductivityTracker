package com.productivitytracker.service;

import com.productivitytracker.dao.ReminderDAO;
import com.productivitytracker.model.Reminder;
import com.productivitytracker.util.Logger;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ReminderService {

    private final ReminderDAO reminderDAO = new ReminderDAO();

    public boolean addReminder(int userId, String type, String title, String remindAt, String channel) {
        try {
            if (userId <= 0 || title == null || title.isBlank()) {
                return false;
            }
            Timestamp timestamp = Timestamp.valueOf(LocalDateTime.parse(remindAt));
            return reminderDAO.addReminder(userId, normalizeType(type), null, title.trim(), timestamp, normalizeChannel(channel));
        } catch (DateTimeParseException | IllegalArgumentException ex) {
            Logger.logError("Invalid reminder input", ex);
            return false;
        }
    }

    public List<Reminder> getReminders(int userId) {
        if (userId <= 0) {
            return List.of();
        }
        return reminderDAO.getReminders(userId);
    }

    public List<Reminder> getDueRemindersForUser(int userId) {
        if (userId <= 0) {
            return List.of();
        }
        return reminderDAO.getDueRemindersForUser(userId);
    }

    public boolean dismissReminder(long reminderId, int userId) {
        return reminderId > 0 && userId > 0 && reminderDAO.dismissReminder(reminderId, userId);
    }

    public boolean deleteReminder(long reminderId, int userId) {
        return reminderId > 0 && userId > 0 && reminderDAO.deleteReminder(reminderId, userId);
    }

    private String normalizeType(String type) {
        if (type == null) {
            return "GENERAL";
        }
        String normalized = type.trim().toUpperCase();
        return switch (normalized) {
            case "TASK", "HABIT", "GOAL" -> normalized;
            default -> "GENERAL";
        };
    }

    private String normalizeChannel(String channel) {
        if (channel == null) {
            return "IN_APP";
        }
        String normalized = channel.trim().toUpperCase();
        return switch (normalized) {
            case "EMAIL", "BOTH" -> normalized;
            default -> "IN_APP";
        };
    }
}
