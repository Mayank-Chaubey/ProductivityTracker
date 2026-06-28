package com.productivitytracker.dto;

import java.time.LocalDateTime;

/**
 * One completed pomodoro session.
 */
public class PomodoroSessionDTO {

    private final int id;
    private final int workMinutes;
    private final int breakMinutes;
    private final int cycles;
    private final int totalFocusMinutes;
    private final LocalDateTime completedAt;

    public PomodoroSessionDTO(int id, int workMinutes, int breakMinutes, int cycles,
                              int totalFocusMinutes, LocalDateTime completedAt) {
        this.id = id;
        this.workMinutes = workMinutes;
        this.breakMinutes = breakMinutes;
        this.cycles = cycles;
        this.totalFocusMinutes = totalFocusMinutes;
        this.completedAt = completedAt;
    }

    public int getId() {
        return id;
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
