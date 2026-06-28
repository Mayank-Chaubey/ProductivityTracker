package com.productivitytracker.dto;

/**
 * Habit streak data prepared for dashboard and reports.
 */
public class StreakDTO {

    private final int habitId;
    private final String habitName;
    private final int weeklyStreak;
    private final int currentStreak;
    private final int longestStreak;
    private final int freezeTokens;
    private final String lastCompletedDate;

    public StreakDTO(int habitId, String habitName, int weeklyStreak, int currentStreak,
                     int longestStreak, int freezeTokens, String lastCompletedDate) {
        this.habitId = habitId;
        this.habitName = habitName;
        this.weeklyStreak = weeklyStreak;
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
        this.freezeTokens = freezeTokens;
        this.lastCompletedDate = lastCompletedDate;
    }

    public int getHabitId() {
        return habitId;
    }

    public String getHabitName() {
        return habitName;
    }

    public int getWeeklyStreak() {
        return weeklyStreak;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public int getLongestStreak() {
        return longestStreak;
    }

    public int getFreezeTokens() {
        return freezeTokens;
    }

    public String getLastCompletedDate() {
        return lastCompletedDate;
    }
}
