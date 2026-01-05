package com.productivitytracker.model;

public class Report {

    private int totalTasks;
    private int completedTasks;
    private int totalHabits;
    private int totalActivities;
    private int totalTimeMinutes;
    private int productivityPercentage;

    public Report() {}

    public Report(int totalTasks, int completedTasks,
                  int totalHabits, int totalActivities,
                  int totalTimeMinutes, int productivityPercentage) {

        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.totalHabits = totalHabits;
        this.totalActivities = totalActivities;
        this.totalTimeMinutes = totalTimeMinutes;
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

    public int getTotalTimeMinutes() {
        return totalTimeMinutes;
    }

    public int getProductivityPercentage() {
        return productivityPercentage;
    }

    @Override
    public String toString() {
        return "Report{" +
                "totalTasks=" + totalTasks +
                ", completedTasks=" + completedTasks +
                ", totalHabits=" + totalHabits +
                ", totalActivities=" + totalActivities +
                ", totalTimeMinutes=" + totalTimeMinutes +
                ", productivityPercentage=" + productivityPercentage +
                '}';
    }
}