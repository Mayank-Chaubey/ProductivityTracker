package com.productivitytracker.model;

import java.sql.Timestamp;

public class Reminder {

    private long id;
    private int userId;
    private String reminderType;
    private Integer referenceId;
    private String title;
    private Timestamp remindAt;
    private String channel;
    private String status;

    public Reminder() {}

    public Reminder(long id, int userId, String reminderType, Integer referenceId, String title,
                    Timestamp remindAt, String channel, String status) {
        this.id = id;
        this.userId = userId;
        this.reminderType = reminderType;
        this.referenceId = referenceId;
        this.title = title;
        this.remindAt = remindAt;
        this.channel = channel;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getReminderType() {
        return reminderType;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public String getTitle() {
        return title;
    }

    public Timestamp getRemindAt() {
        return remindAt;
    }

    public String getChannel() {
        return channel;
    }

    public String getStatus() {
        return status;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setRemindAt(Timestamp remindAt) {
        this.remindAt = remindAt;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
