package com.productivitytracker.model;

import java.sql.Date;

public class TimeLog {

    private int id;
    private int userId;
    private String referenceType; // ACTIVITY / TASK / HABIT
    private int referenceId;
    private int durationMinutes;
    private Date logDate;

    public TimeLog() {}

    public TimeLog(int id, int userId, String referenceType,
                   int referenceId, int durationMinutes, Date logDate) {
        this.id = id;
        this.userId = userId;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.durationMinutes = durationMinutes;
        this.logDate = logDate;
    }

    // Convenience constructor (insert)
    public TimeLog(int userId, String referenceType,
                   int referenceId, int durationMinutes) {
        this.userId = userId;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.durationMinutes = durationMinutes;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getReferenceType() { return referenceType; }
    public int getReferenceId() { return referenceId; }
    public int getDurationMinutes() { return durationMinutes; }
    public Date getLogDate() { return logDate; }
}