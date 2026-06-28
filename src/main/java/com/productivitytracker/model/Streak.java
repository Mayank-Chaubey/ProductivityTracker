package com.productivitytracker.model;

import java.sql.Date;

/**
 * Represents streak information for a habit.
 */
public class Streak {

    private int habitId;
    private int userId;
    private String habitName;
    private int weeklyStreak;
    private int currentStreak;
    private int longestStreak;
    private int freezeTokens;
    private Date lastCompletedDate;

    public Streak() {}

    public Streak(int habitId, int userId, String habitName,
                  int weeklyStreak, int currentStreak, int longestStreak,
                  int freezeTokens, Date lastCompletedDate) {
        this.habitId = habitId;
        this.userId = userId;
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

    public int getUserId() {
        return userId;
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

    public Date getLastCompletedDate() {
        return lastCompletedDate;
    }

    public void setHabitId(int habitId) {
        this.habitId = habitId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setHabitName(String habitName) {
        this.habitName = habitName;
    }

    public void setWeeklyStreak(int weeklyStreak) {
        this.weeklyStreak = weeklyStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public void setLongestStreak(int longestStreak) {
        this.longestStreak = longestStreak;
    }

    public void setFreezeTokens(int freezeTokens) {
        this.freezeTokens = freezeTokens;
    }

    public void setLastCompletedDate(Date lastCompletedDate) {
        this.lastCompletedDate = lastCompletedDate;
    }
}
