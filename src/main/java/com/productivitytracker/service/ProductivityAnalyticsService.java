package com.productivitytracker.service;

import com.productivitytracker.dao.ReportDAO;
import com.productivitytracker.dao.TimeLogDAO;
import com.productivitytracker.dto.ReportDTO;
import com.productivitytracker.exception.ValidationException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Provides reusable productivity analytics for dashboard and reports.
 */
public class ProductivityAnalyticsService {

    private static final int MINUTES_TARGET_PER_DAY = 120;

    private final ReportDAO reportDAO;
    private final TimeLogDAO timeLogDAO;

    public ProductivityAnalyticsService() {
        this.reportDAO = new ReportDAO();
        this.timeLogDAO = new TimeLogDAO();
    }

    public int calculatePercentage(int completed, int total) {
        if (total <= 0) {
            return 0;
        }
        return Math.min(100, Math.max(0, (completed * 100) / total));
    }

    public int calculateActivityScore(int totalMinutes) {
        return calculatePercentage(totalMinutes, MINUTES_TARGET_PER_DAY);
    }

    public int calculateOverallProductivity(int taskScore, int habitScore, int activityScore) {
        return (int) Math.round((taskScore * 0.45) + (habitScore * 0.35) + (activityScore * 0.20));
    }

    public ReportDTO buildReport(int userId, int days) {
        validateUserId(userId);

        int totalTasks = reportDAO.getTotalTasks(userId);
        int completedTasks = reportDAO.getCompletedTasks(userId);
        int totalHabits = reportDAO.getTotalHabits(userId);
        int completedHabitsToday = reportDAO.getCompletedHabitsToday(userId);
        int totalActivities = reportDAO.getTodayActivitiesCount(userId);
        int totalTimeMinutes = resolveTodayMinutes(userId);

        int taskScore = calculatePercentage(completedTasks, totalTasks);
        int habitScore = calculatePercentage(completedHabitsToday, totalHabits);
        int activityScore = calculateActivityScore(totalTimeMinutes);
        int productivity = calculateOverallProductivity(taskScore, habitScore, activityScore);

        return new ReportDTO(
                totalTasks,
                completedTasks,
                totalHabits,
                completedHabitsToday,
                totalActivities,
                totalTimeMinutes,
                productivity,
                habitScore,
                buildDailyReports(userId, days)
        );
    }

    public List<ReportDTO.DailyReportDTO> buildDailyReports(int userId, int days) {
        validateUserId(userId);

        int safeDays = Math.max(1, Math.min(days, 31));
        List<ReportDTO.DailyReportDTO> reports = new ArrayList<>();
        LocalDate startDate = LocalDate.now().minusDays(safeDays - 1L);

        for (int index = 0; index < safeDays; index++) {
            LocalDate date = startDate.plusDays(index);
            int completedTasks = reportDAO.getCompletedTasksForDate(userId, date);
            int timeLogged = reportDAO.getActivityMinutesForDate(userId, date);
            int activityScore = calculateActivityScore(timeLogged);
            int productivity = calculateOverallProductivity(
                    completedTasks > 0 ? 100 : 0,
                    0,
                    activityScore
            );

            reports.add(new ReportDTO.DailyReportDTO(dayLabel(date), productivity, completedTasks, timeLogged));
        }

        return reports;
    }

    private int resolveTodayMinutes(int userId) {
        int loggedMinutes = timeLogDAO.getTotalTimeForToday(userId);
        if (loggedMinutes > 0) {
            return loggedMinutes;
        }
        return reportDAO.getTodayTotalTime(userId);
    }

    private String dayLabel(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
    }

    private void validateUserId(int userId) {
        if (userId <= 0) {
            throw new ValidationException("Invalid user ID");
        }
    }
}
