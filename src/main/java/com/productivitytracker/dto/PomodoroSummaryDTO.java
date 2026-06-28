package com.productivitytracker.dto;

import java.util.List;

/**
 * Aggregated pomodoro metrics for dashboard and reports.
 */
public class PomodoroSummaryDTO {

    private final int sessionsToday;
    private final int focusMinutesToday;
    private final int breakMinutesToday;
    private final int totalSessionsLifetime;
    private final List<PomodoroSessionDTO> recentSessions;

    public PomodoroSummaryDTO(int sessionsToday, int focusMinutesToday, int breakMinutesToday,
                              int totalSessionsLifetime, List<PomodoroSessionDTO> recentSessions) {
        this.sessionsToday = sessionsToday;
        this.focusMinutesToday = focusMinutesToday;
        this.breakMinutesToday = breakMinutesToday;
        this.totalSessionsLifetime = totalSessionsLifetime;
        this.recentSessions = List.copyOf(recentSessions);
    }

    public int getSessionsToday() {
        return sessionsToday;
    }

    public int getFocusMinutesToday() {
        return focusMinutesToday;
    }

    public int getBreakMinutesToday() {
        return breakMinutesToday;
    }

    public int getTotalSessionsLifetime() {
        return totalSessionsLifetime;
    }

    public List<PomodoroSessionDTO> getRecentSessions() {
        return recentSessions;
    }
}
