package com.productivitytracker.dto;

/**
 * Time log data prepared for reporting.
 */
public class TimeLogDTO {

    private final int id;
    private final String referenceType;
    private final int referenceId;
    private final int durationMinutes;
    private final String logDate;

    public TimeLogDTO(int id, String referenceType, int referenceId, int durationMinutes, String logDate) {
        this.id = id;
        this.referenceType = referenceType;
        this.referenceId = referenceId;
        this.durationMinutes = durationMinutes;
        this.logDate = logDate;
    }

    public int getId() {
        return id;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public int getReferenceId() {
        return referenceId;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getLogDate() {
        return logDate;
    }
}
