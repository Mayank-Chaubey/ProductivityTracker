package com.productivitytracker.service;

import com.productivitytracker.dao.ReminderDAO;
import com.productivitytracker.dao.UserDAO;
import com.productivitytracker.model.Reminder;
import com.productivitytracker.model.User;
import com.productivitytracker.model.UserSettings;
import com.productivitytracker.util.Logger;

import java.util.List;

public class NotificationService {

    private final ReminderDAO reminderDAO = new ReminderDAO();
    private final UserDAO userDAO = new UserDAO();
    private final EmailService emailService = new EmailService();

    public void processDueEmailReminders() {
        List<Reminder> dueReminders = reminderDAO.getDueReminders(50);
        for (Reminder reminder : dueReminders) {
            try {
                User user = userDAO.getUserById(reminder.getUserId());
                if (user == null) {
                    continue;
                }
                UserSettings settings = userDAO.getSettings(user.getId());
                if (settings.isEmailNotifications()) {
                    emailService.sendReminderEmail(user, reminder.getTitle());
                }
                reminderDAO.markEmailSent(reminder.getId());
            } catch (Exception ex) {
                Logger.logError("Failed to process due reminder " + reminder.getId(), ex);
            }
        }
    }
}
