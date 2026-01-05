package com.productivitytracker.service;

import java.io.Serializable;

public final class ReportSummary implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int totalTasks;
    private final int completedTasks;
    private final int totalHabits;
    private final int totalActivities;
    private final int productivityPercentage;

    public ReportSummary(
            int totalTasks,
            int completedTasks,
            int totalHabits,
            int totalActivities,
            int productivityPercentage) {

        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.totalHabits = totalHabits;
        this.totalActivities = totalActivities;
        this.productivityPercentage = productivityPercentage;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

    public int getTotalHabits() {
        return totalHabits;
    }

    public int getTotalActivities() {
        return totalActivities;
    }

    public int getProductivityPercentage() {
        return productivityPercentage;
    }

    @Override
    public String toString() {
        return "ReportSummary{" +
                "totalTasks=" + totalTasks +
                ", completedTasks=" + completedTasks +
                ", totalHabits=" + totalHabits +
                ", totalActivities=" + totalActivities +
                ", productivityPercentage=" + productivityPercentage +
                '}';
    }
}