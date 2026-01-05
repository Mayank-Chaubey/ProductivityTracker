package com.productivitytracker.service;

import com.productivitytracker.dao.ReportDAO;
import com.productivitytracker.dao.TimeLogDAO;
import com.productivitytracker.model.Report;

public class ProductivityService {

    private final ReportDAO reportDAO;
    private final TimeLogDAO timeLogDAO;

    public ProductivityService() {
        this.reportDAO = new ReportDAO();
        this.timeLogDAO = new TimeLogDAO();
    }

    // ---------- BUILD TODAY'S REPORT ----------
    public Report buildTodayReport(int userId) {

        if (userId <= 0) {
            return new Report();
        }

        int totalTasks = reportDAO.getTotalTasks(userId);
        int completedTasks = reportDAO.getCompletedTasks(userId);
        int totalHabits = reportDAO.getTotalHabits(userId);
        int totalActivities = reportDAO.getTodayActivitiesCount(userId);
        int totalTimeMinutes = timeLogDAO.getTotalTimeForToday(userId);

        int taskScore = calculatePercentage(completedTasks, totalTasks);
        int habitScore = calculatePercentage(totalHabits, totalHabits); // placeholder
        int activityScore = totalActivities > 0 ? 100 : 0; // simple rule

        int productivity =
                calculateOverallProductivity(taskScore, habitScore, activityScore);

        return new Report(
                totalTasks,
                completedTasks,
                totalHabits,
                totalActivities,
                totalTimeMinutes,
                productivity
        );
    }

    // ---------- CALCULATIONS ----------
    private int calculatePercentage(int completed, int total) {

        if (total <= 0) {
            return 0;
        }

        return (completed * 100) / total;
    }

    private int calculateOverallProductivity(int taskScore,
                                             int habitScore,
                                             int activityScore) {

        int totalWeight = 3;
        return (taskScore + habitScore + activityScore) / totalWeight;
    }
}