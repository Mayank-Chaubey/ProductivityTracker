package com.productivitytracker.dto;

import java.time.LocalDate;

/**
 * Goal data prepared for JSP rendering.
 */
public class GoalDTO {

    private final int id;
    private final String title;
    private final String goalType;
    private final int targetValue;
    private final int currentValue;
    private final int progressPercentage;
    private final String status;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public GoalDTO(int id, String title, String goalType, int targetValue, int currentValue,
                   int progressPercentage, String status, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.title = title;
        this.goalType = goalType;
        this.targetValue = targetValue;
        this.currentValue = currentValue;
        this.progressPercentage = progressPercentage;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
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

    public int getProgressPercentage() {
        return progressPercentage;
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
