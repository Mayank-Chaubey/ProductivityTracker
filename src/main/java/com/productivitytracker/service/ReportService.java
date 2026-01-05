package com.productivitytracker.service;

import com.productivitytracker.dao.ReportDAO;
import com.productivitytracker.dao.TimeLogDAO;
import com.productivitytracker.model.Report;
import com.productivitytracker.util.Logger;

public class ReportService {

    private ReportDAO reportDAO = new ReportDAO();
    private TimeLogDAO timeLogDAO = new TimeLogDAO();

    public ReportService() {
        this.reportDAO = new ReportDAO();
        this.timeLogDAO = new TimeLogDAO();
    }

    // ---------- PUBLIC CALCULATORS (used by servlets/tests) ----------
    public int calculateTaskProductivity(int completedTasks, int totalTasks) {
        try {
            return calculatePercentage(completedTasks, totalTasks);
        } catch (Exception e) {
            Logger.logError("Service error in calculateTaskProductivity", e);
            return 0;
        }
    }

    public int calculateHabitConsistency(int completedHabits, int totalHabits) {
        try {
            return calculatePercentage(completedHabits, totalHabits);
        } catch (Exception e) {
            Logger.logError("Service error in calculateHabitConsistency", e);
            return 0;
        }
    }

    public int calculateOverallProductivity(int taskScore,
                                            int habitScore,
                                            int activityScore) {
        try {
            int totalWeight = 3;
            return (taskScore + habitScore + activityScore) / totalWeight;
        } catch (Exception e) {
            Logger.logError("Service error in calculateOverallProductivity", e);
            return 0;
        }
    }

    // ---------- BUILD TODAY'S REPORT ----------
    public Report buildTodayReport(int userId) {
        try {
            if (userId <= 0) {
                return new Report();
            }
            int totalTasks = reportDAO.getTotalTasks(userId);
            int completedTasks = reportDAO.getCompletedTasks(userId);
            int totalHabits = reportDAO.getTotalHabits(userId);
            int totalActivities = reportDAO.getTodayActivitiesCount(userId);
            int totalTimeMinutes = timeLogDAO.getTotalTimeForToday(userId);
            int taskScore = calculateTaskProductivity(completedTasks, totalTasks);
            int habitScore = calculateHabitConsistency(totalHabits, totalHabits); // placeholder
            int activityScore = totalActivities > 0 ? 100 : 0;
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
        } catch (Exception e) {
            Logger.logError("Service error in buildTodayReport", e);
            return new Report();
        }
    }

    // ---------- INTERNAL HELPER ----------
    private int calculatePercentage(int completed, int total) {
        if (total <= 0) {
            return 0;
        }
        return (completed * 100) / total;
    }
}