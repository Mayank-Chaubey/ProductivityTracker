package com.productivitytracker.dto;

import java.util.List;

/**
 * Productivity report data prepared for reports.jsp.
 */
public class ReportDTO {

    private final int totalTasks;
    private final int completedTasks;
    private final int totalHabits;
    private final int completedHabitsToday;
    private final int totalActivities;
    private final int totalTimeMinutes;
    private final int productivityPercentage;
    private final int habitConsistencyPercentage;
    private final List<DailyReportDTO> weeklyReports;
    private final List<AnalyticsPointDTO> productivityTrend;
    private final List<AnalyticsPointDTO> weeklyBars;
    private final List<HeatmapCellDTO> heatmap;

    public ReportDTO(int totalTasks, int completedTasks, int totalHabits, int completedHabitsToday,
                     int totalActivities, int totalTimeMinutes, int productivityPercentage,
                     int habitConsistencyPercentage, List<DailyReportDTO> weeklyReports) {
        this(totalTasks, completedTasks, totalHabits, completedHabitsToday, totalActivities, totalTimeMinutes,
                productivityPercentage, habitConsistencyPercentage, weeklyReports, List.of(), List.of(), List.of());
    }

    public ReportDTO(int totalTasks, int completedTasks, int totalHabits, int completedHabitsToday,
                     int totalActivities, int totalTimeMinutes, int productivityPercentage,
                     int habitConsistencyPercentage, List<DailyReportDTO> weeklyReports,
                     List<AnalyticsPointDTO> productivityTrend, List<AnalyticsPointDTO> weeklyBars,
                     List<HeatmapCellDTO> heatmap) {
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.totalHabits = totalHabits;
        this.completedHabitsToday = completedHabitsToday;
        this.totalActivities = totalActivities;
        this.totalTimeMinutes = totalTimeMinutes;
        this.productivityPercentage = productivityPercentage;
        this.habitConsistencyPercentage = habitConsistencyPercentage;
        this.weeklyReports = List.copyOf(weeklyReports);
        this.productivityTrend = List.copyOf(productivityTrend);
        this.weeklyBars = List.copyOf(weeklyBars);
        this.heatmap = List.copyOf(heatmap);
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

    public int getCompletedHabitsToday() {
        return completedHabitsToday;
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

    public int getHabitConsistencyPercentage() {
        return habitConsistencyPercentage;
    }

    public List<DailyReportDTO> getWeeklyReports() {
        return weeklyReports;
    }

    public List<AnalyticsPointDTO> getProductivityTrend() {
        return productivityTrend;
    }

    public List<AnalyticsPointDTO> getWeeklyBars() {
        return weeklyBars;
    }

    public List<HeatmapCellDTO> getHeatmap() {
        return heatmap;
    }

    /**
     * One row in a daily productivity breakdown.
     */
    public static class DailyReportDTO {

        private final String day;
        private final int productivity;
        private final int tasksCompleted;
        private final int timeLogged;

        public DailyReportDTO(String day, int productivity, int tasksCompleted, int timeLogged) {
            this.day = day;
            this.productivity = productivity;
            this.tasksCompleted = tasksCompleted;
            this.timeLogged = timeLogged;
        }

        public String getDay() {
            return day;
        }

        public int getProductivity() {
            return productivity;
        }

        public int getTasksCompleted() {
            return tasksCompleted;
        }

        public int getTimeLogged() {
            return timeLogged;
        }
    }
}
