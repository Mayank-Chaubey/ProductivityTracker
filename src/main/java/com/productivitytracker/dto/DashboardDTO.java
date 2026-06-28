package com.productivitytracker.dto;

import java.util.List;

/**
 * Aggregated data used by the dashboard page.
 */
public class DashboardDTO {

    private final int productivity;
    private final int completedTasks;
    private final int totalTasks;
    private final int completedHabitsToday;
    private final int totalHabits;
    private final int activitiesLoggedToday;
    private final int totalTimeMinutesToday;
    private final int bestStreak;
    private final int weeklyStreak;
    private final int freezeTokens;
    private final int pomodoroSessionsToday;
    private final int focusMinutesToday;
    private final int completedGoals;
    private final int totalGoals;
    private final List<TaskDTO> todayTasks;
    private final List<HabitDTO> habits;
    private final List<StreakDTO> streaks;
    private final List<GoalDTO> goals;
    private final List<AchievementDTO> achievements;
    private final List<AnalyticsPointDTO> weeklyProductivity;

    public DashboardDTO(int productivity, int completedTasks, int totalTasks,
                        int completedHabitsToday, int totalHabits, int activitiesLoggedToday,
                        int totalTimeMinutesToday, int bestStreak, List<TaskDTO> todayTasks,
                        List<HabitDTO> habits, List<StreakDTO> streaks) {
        this(productivity, completedTasks, totalTasks, completedHabitsToday, totalHabits, activitiesLoggedToday,
                totalTimeMinutesToday, bestStreak, 0, 0, 0, totalTimeMinutesToday, 0, 0,
                todayTasks, habits, streaks, List.of(), List.of(), List.of());
    }

    public DashboardDTO(int productivity, int completedTasks, int totalTasks,
                        int completedHabitsToday, int totalHabits, int activitiesLoggedToday,
                        int totalTimeMinutesToday, int bestStreak, int weeklyStreak, int freezeTokens,
                        int pomodoroSessionsToday, int focusMinutesToday, int completedGoals, int totalGoals,
                        List<TaskDTO> todayTasks, List<HabitDTO> habits, List<StreakDTO> streaks,
                        List<GoalDTO> goals, List<AchievementDTO> achievements,
                        List<AnalyticsPointDTO> weeklyProductivity) {
        this.productivity = productivity;
        this.completedTasks = completedTasks;
        this.totalTasks = totalTasks;
        this.completedHabitsToday = completedHabitsToday;
        this.totalHabits = totalHabits;
        this.activitiesLoggedToday = activitiesLoggedToday;
        this.totalTimeMinutesToday = totalTimeMinutesToday;
        this.bestStreak = bestStreak;
        this.weeklyStreak = weeklyStreak;
        this.freezeTokens = freezeTokens;
        this.pomodoroSessionsToday = pomodoroSessionsToday;
        this.focusMinutesToday = focusMinutesToday;
        this.completedGoals = completedGoals;
        this.totalGoals = totalGoals;
        this.todayTasks = List.copyOf(todayTasks);
        this.habits = List.copyOf(habits);
        this.streaks = List.copyOf(streaks);
        this.goals = List.copyOf(goals);
        this.achievements = List.copyOf(achievements);
        this.weeklyProductivity = List.copyOf(weeklyProductivity);
    }

    public int getProductivity() {
        return productivity;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public int getCompletedHabitsToday() {
        return completedHabitsToday;
    }

    public int getTotalHabits() {
        return totalHabits;
    }

    public int getActivitiesLoggedToday() {
        return activitiesLoggedToday;
    }

    public int getTotalTimeMinutesToday() {
        return totalTimeMinutesToday;
    }

    public int getBestStreak() {
        return bestStreak;
    }

    public int getWeeklyStreak() {
        return weeklyStreak;
    }

    public int getFreezeTokens() {
        return freezeTokens;
    }

    public int getPomodoroSessionsToday() {
        return pomodoroSessionsToday;
    }

    public int getFocusMinutesToday() {
        return focusMinutesToday;
    }

    public int getCompletedGoals() {
        return completedGoals;
    }

    public int getTotalGoals() {
        return totalGoals;
    }

    public List<TaskDTO> getTodayTasks() {
        return todayTasks;
    }

    public List<HabitDTO> getHabits() {
        return habits;
    }

    public List<StreakDTO> getStreaks() {
        return streaks;
    }

    public List<GoalDTO> getGoals() {
        return goals;
    }

    public List<AchievementDTO> getAchievements() {
        return achievements;
    }

    public List<AnalyticsPointDTO> getWeeklyProductivity() {
        return weeklyProductivity;
    }
}
