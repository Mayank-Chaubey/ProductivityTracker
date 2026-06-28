package com.productivitytracker.model;

import java.time.LocalDate;

/**
 * Goal entity for monthly/quarterly targets.
 */
public class Goal {

    private int id;
    private int userId;
    private String title;
    private String goalType;
    private int targetValue;
    private int currentValue;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;

    public Goal() {
    }

    public Goal(int id, int userId, String title, String goalType, int targetValue,
                int currentValue, String status, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.goalType = goalType;
        this.targetValue = targetValue;
        this.currentValue = currentValue;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getGoalType() {
        return goalType;
    }

    public int getTargetValue() {
        return targetValue;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
