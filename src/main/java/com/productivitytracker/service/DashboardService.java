package com.productivitytracker.service;

import com.productivitytracker.dao.ActivityDAO;
import com.productivitytracker.dao.HabitDAO;
import com.productivitytracker.dao.TaskDAO;
import com.productivitytracker.dto.DashboardDTO;
import com.productivitytracker.dto.HabitDTO;
import com.productivitytracker.dto.ReportDTO;
import com.productivitytracker.dto.StreakDTO;
import com.productivitytracker.dto.TaskDTO;
import com.productivitytracker.exception.ValidationException;
import com.productivitytracker.mapper.HabitMapper;
import com.productivitytracker.mapper.TaskMapper;
import com.productivitytracker.model.Habit;
import com.productivitytracker.model.Task;
import com.productivitytracker.util.Logger;

import java.util.List;

/**
 * Builds dashboard-specific aggregates without exposing DAO models to servlets.
 */
public class DashboardService {

    private final TaskDAO taskDAO;
    private final HabitDAO habitDAO;
    private final ActivityDAO activityDAO;
    private final ReportService reportService;
    private final StreakService streakService;
    private final GoalService goalService;
    private final AchievementService achievementService;
    private final PomodoroService pomodoroService;
    private final AnalyticsService analyticsService;

    public DashboardService() {
        this.taskDAO = new TaskDAO();
        this.habitDAO = new HabitDAO();
        this.activityDAO = new ActivityDAO();
        this.reportService = new ReportService();
        this.streakService = new StreakService();
        this.goalService = new GoalService();
        this.achievementService = new AchievementService();
        this.pomodoroService = new PomodoroService();
        this.analyticsService = new AnalyticsService();
    }

    public DashboardDTO buildDashboard(int userId) {
        try {
            validateUserId(userId);

            List<Task> tasks = taskDAO.getTasks(userId);
            List<Habit> habits = habitDAO.getHabits(userId);
            ReportDTO report = reportService.buildTodayReportDTO(userId);
            List<StreakDTO> streaks = streakService.getUserStreaks(userId);
            var goals = goalService.getActiveGoals(userId);
            var achievements = achievementService.evaluateAndGetAchievements(userId);
            var pomodoroSummary = pomodoroService.getSummary(userId);

            List<TaskDTO> taskDTOs = TaskMapper.toDTOList(tasks);
            List<HabitDTO> habitDTOs = HabitMapper.toDTOList(habits);
            int totalTime = report.getTotalTimeMinutes() + pomodoroSummary.getFocusMinutesToday();

            return new DashboardDTO(
                    report.getProductivityPercentage(),
                    report.getCompletedTasks(),
                    report.getTotalTasks(),
                    report.getCompletedHabitsToday(),
                    report.getTotalHabits(),
                    activityDAO.getTodayActivities(userId).size(),
                    totalTime,
                    streakService.getBestStreak(userId),
                    streakService.getWeeklyBestStreak(userId),
                    streakService.getFreezeTokens(userId),
                    pomodoroSummary.getSessionsToday(),
                    pomodoroSummary.getFocusMinutesToday(),
                    goalService.countCompletedGoals(userId),
                    goalService.countTotalGoals(userId),
                    taskDTOs.stream().limit(5).toList(),
                    habitDTOs.stream().limit(5).toList(),
                    streaks.stream().limit(5).toList(),
                    goals.stream().limit(3).toList(),
                    achievements.stream().limit(6).toList(),
                    analyticsService.buildProductivityTrend(userId, 7)
            );
        } catch (Exception ex) {
            Logger.logError("Service error in buildDashboard", ex);
            return new DashboardDTO(0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0,
                    List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
        }
    }

    private void validateUserId(int userId) {
        if (userId <= 0) {
            throw new ValidationException("Invalid user ID");
        }
    }
}
