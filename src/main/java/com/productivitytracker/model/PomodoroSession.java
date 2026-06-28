package com.productivitytracker.model;

import java.time.LocalDateTime;

/**
 * Completed pomodoro session log.
 */
public class PomodoroSession {

    private int id;
    private int userId;
    private int workMinutes;
    private int breakMinutes;
    private int cycles;
    private int totalFocusMinutes;
    private LocalDateTime completedAt;

    public PomodoroSession() {
    }

    public PomodoroSession(int id, int userId, int workMinutes, int breakMinutes, int cycles,
                           int totalFocusMinutes, LocalDateTime completedAt) {
        this.id = id;
        this.userId = userId;
        this.workMinutes = workMinutes;
        this.breakMinutes = breakMinutes;
        this.cycles = cycles;
        this.totalFocusMinutes = totalFocusMinutes;
        this.completedAt = completedAt;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getWorkMinutes() {
        return workMinutes;
    }

    public int getBreakMinutes() {
        return breakMinutes;
    }

    public int getCycles() {
        return cycles;
    }

    public int getTotalFocusMinutes() {
        return totalFocusMinutes;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
}
