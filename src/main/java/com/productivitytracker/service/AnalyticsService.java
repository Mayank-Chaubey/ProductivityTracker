package com.productivitytracker.service;

import com.productivitytracker.dao.PomodoroDAO;
import com.productivitytracker.dao.ReportDAO;
import com.productivitytracker.dto.AnalyticsPointDTO;
import com.productivitytracker.dto.HeatmapCellDTO;
import com.productivitytracker.exception.ValidationException;
import com.productivitytracker.util.Logger;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Analytics builder for dashboard and report charts.
 */
public class AnalyticsService {

    private static final int FOCUS_TARGET_MINUTES = 120;

    private final ReportDAO reportDAO;
    private final PomodoroDAO pomodoroDAO;

    public AnalyticsService() {
        this.reportDAO = new ReportDAO();
        this.pomodoroDAO = new PomodoroDAO();
    }

    public List<AnalyticsPointDTO> buildProductivityTrend(int userId, int days) {
        try {
            validateUserId(userId);
            int safeDays = Math.max(3, Math.min(days, 90));
            List<AnalyticsPointDTO> points = new ArrayList<>();
            LocalDate start = LocalDate.now().minusDays(safeDays - 1L);

            for (int i = 0; i < safeDays; i++) {
                LocalDate date = start.plusDays(i);
                int completedTasks = reportDAO.getCompletedTasksForDate(userId, date);
                int completedHabits = reportDAO.getCompletedHabitsForDate(userId, date);
                int focusMinutes = reportDAO.getActivityMinutesForDate(userId, date) + pomodoroDAO.getFocusMinutesForDate(userId, date);

                int taskScore = completedTasks > 0 ? 100 : 0;
                int habitScore = completedHabits > 0 ? 100 : 0;
                int focusScore = Math.min(100, (focusMinutes * 100) / FOCUS_TARGET_MINUTES);
                int productivity = (int) Math.round((taskScore * 0.45) + (habitScore * 0.30) + (focusScore * 0.25));

                points.add(new AnalyticsPointDTO(shortDayLabel(date), productivity));
            }
            return points;
        } catch (RuntimeException ex) {
            Logger.logError("Service error in buildProductivityTrend", ex);
            return List.of();
        }
    }

    public List<AnalyticsPointDTO> buildWeeklyBars(int userId) {
        try {
            validateUserId(userId);
            LocalDate today = LocalDate.now();
            LocalDate weekStart = today.minusDays(6);

            int completedTasks = 0;
            int completedHabits = 0;
            int focusMinutes = 0;

            for (int i = 0; i < 7; i++) {
                LocalDate date = weekStart.plusDays(i);
                completedTasks += reportDAO.getCompletedTasksForDate(userId, date);
                completedHabits += reportDAO.getCompletedHabitsForDate(userId, date);
                focusMinutes += reportDAO.getActivityMinutesForDate(userId, date);
                focusMinutes += pomodoroDAO.getFocusMinutesForDate(userId, date);
            }

            List<AnalyticsPointDTO> bars = new ArrayList<>();
            bars.add(new AnalyticsPointDTO("Tasks", completedTasks));
            bars.add(new AnalyticsPointDTO("Habits", completedHabits));
            bars.add(new AnalyticsPointDTO("Focus", focusMinutes));
            return bars;
        } catch (RuntimeException ex) {
            Logger.logError("Service error in buildWeeklyBars", ex);
            return List.of();
        }
    }

    public List<HeatmapCellDTO> buildHeatmap(int userId, int days) {
        try {
            validateUserId(userId);
            int safeDays = Math.max(28, Math.min(days, 365));
            List<HeatmapCellDTO> cells = new ArrayList<>();
            LocalDate start = LocalDate.now().minusDays(safeDays - 1L);

            for (int i = 0; i < safeDays; i++) {
                LocalDate date = start.plusDays(i);
                int minutes = reportDAO.getActivityMinutesForDate(userId, date) + pomodoroDAO.getFocusMinutesForDate(userId, date);
                int intensity;
                if (minutes <= 0) {
                    intensity = 0;
                } else if (minutes < 30) {
                    intensity = 1;
                } else if (minutes < 60) {
                    intensity = 2;
                } else if (minutes < 120) {
                    intensity = 3;
                } else {
                    intensity = 4;
                }
                cells.add(new HeatmapCellDTO(date, intensity));
            }
            return cells;
        } catch (RuntimeException ex) {
            Logger.logError("Service error in buildHeatmap", ex);
            return List.of();
        }
    }

    private String shortDayLabel(LocalDate date) {
        return date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
    }

    private void validateUserId(int userId) {
        if (userId <= 0) {
            throw new ValidationException("Invalid user ID");
        }
    }
}
