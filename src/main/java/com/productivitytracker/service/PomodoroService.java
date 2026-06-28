package com.productivitytracker.service;

import com.productivitytracker.dao.PomodoroDAO;
import com.productivitytracker.dto.PomodoroSessionDTO;
import com.productivitytracker.dto.PomodoroSummaryDTO;
import com.productivitytracker.exception.ValidationException;
import com.productivitytracker.model.PomodoroSession;
import com.productivitytracker.util.Logger;

import java.util.List;

/**
 * Service for pomodoro timer logging and aggregates.
 */
public class PomodoroService {

    private final PomodoroDAO pomodoroDAO;

    public PomodoroService() {
        this.pomodoroDAO = new PomodoroDAO();
    }

    public boolean logSession(int userId, int workMinutes, int breakMinutes, int cycles) {
        try {
            validateUserId(userId);
            if (workMinutes < 15 || workMinutes > 90) {
                throw new ValidationException("Work minutes must be between 15 and 90");
            }
            if (breakMinutes < 1 || breakMinutes > 30) {
                throw new ValidationException("Break minutes must be between 1 and 30");
            }
            if (cycles < 1 || cycles > 10) {
                throw new ValidationException("Cycles must be between 1 and 10");
            }
            int totalFocus = workMinutes * cycles;
            return pomodoroDAO.logSession(userId, workMinutes, breakMinutes, cycles, totalFocus);
        } catch (RuntimeException ex) {
            Logger.logError("Service error in logSession", ex);
            return false;
        }
    }

    public PomodoroSummaryDTO getSummary(int userId) {
        try {
            validateUserId(userId);
            List<PomodoroSessionDTO> recentSessions = pomodoroDAO.getRecentSessions(userId, 7).stream()
                    .map(this::toDTO)
                    .toList();
            return new PomodoroSummaryDTO(
                    pomodoroDAO.getTodaySessionCount(userId),
                    pomodoroDAO.getTodayFocusMinutes(userId),
                    pomodoroDAO.getTodayBreakMinutes(userId),
                    pomodoroDAO.getTotalSessionCount(userId),
                    recentSessions
            );
        } catch (RuntimeException ex) {
            Logger.logError("Service error in getSummary", ex);
            return new PomodoroSummaryDTO(0, 0, 0, 0, List.of());
        }
    }

    private PomodoroSessionDTO toDTO(PomodoroSession session) {
        return new PomodoroSessionDTO(
                session.getId(),
                session.getWorkMinutes(),
                session.getBreakMinutes(),
                session.getCycles(),
                session.getTotalFocusMinutes(),
                session.getCompletedAt()
        );
    }

    private void validateUserId(int userId) {
        if (userId <= 0) {
            throw new ValidationException("Invalid user ID");
        }
    }
}
