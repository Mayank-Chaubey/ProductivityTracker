package com.productivitytracker.model;

public class UserSettings {

    private int userId;
    private boolean emailNotifications;
    private boolean browserNotifications;
    private int reminderLeadMinutes;
    private String timezone;

    public UserSettings() {
        this.emailNotifications = true;
        this.browserNotifications = true;
        this.reminderLeadMinutes = 10;
        this.timezone = "UTC";
    }

    public UserSettings(int userId, boolean emailNotifications, boolean browserNotifications,
                        int reminderLeadMinutes, String timezone) {
        this.userId = userId;
        this.emailNotifications = emailNotifications;
        this.browserNotifications = browserNotifications;
        this.reminderLeadMinutes = reminderLeadMinutes;
        this.timezone = timezone;
    }

    public int getUserId() {
        return userId;
    }

    public boolean isEmailNotifications() {
        return emailNotifications;
    }

    public boolean isBrowserNotifications() {
        return browserNotifications;
    }

    public int getReminderLeadMinutes() {
        return reminderLeadMinutes;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setEmailNotifications(boolean emailNotifications) {
        this.emailNotifications = emailNotifications;
    }

    public void setBrowserNotifications(boolean browserNotifications) {
        this.browserNotifications = browserNotifications;
    }

    public void setReminderLeadMinutes(int reminderLeadMinutes) {
        this.reminderLeadMinutes = reminderLeadMinutes;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
}
