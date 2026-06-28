package com.productivitytracker.service;

import com.productivitytracker.dao.StreakDAO;
import com.productivitytracker.dto.StreakDTO;
import com.productivitytracker.exception.ValidationException;
import com.productivitytracker.mapper.StreakMapper;
import com.productivitytracker.util.Logger;

import java.util.List;

/**
 * Business service for habit streak tracking.
 */
public class StreakService {

    private final StreakDAO streakDAO;

    public StreakService() {
        this.streakDAO = new StreakDAO();
    }

    public List<StreakDTO> getUserStreaks(int userId) {
        try {
            validateUserId(userId);
            return StreakMapper.toDTOList(streakDAO.getStreaksForUser(userId));
        } catch (RuntimeException ex) {
            Logger.logError("Service error in getUserStreaks", ex);
            return List.of();
        }
    }

    public int getBestStreak(int userId) {
        try {
            validateUserId(userId);
            return streakDAO.getBestStreak(userId);
        } catch (RuntimeException ex) {
            Logger.logError("Service error in getBestStreak", ex);
            return 0;
        }
    }

    public int getWeeklyBestStreak(int userId) {
        try {
            validateUserId(userId);
            return streakDAO.getWeeklyBestStreak(userId);
        } catch (RuntimeException ex) {
            Logger.logError("Service error in getWeeklyBestStreak", ex);
            return 0;
        }
    }

    public int getFreezeTokens(int userId) {
        try {
            validateUserId(userId);
            return streakDAO.getTotalFreezeTokens(userId);
        } catch (RuntimeException ex) {
            Logger.logError("Service error in getFreezeTokens", ex);
            return 0;
        }
    }

    public boolean markHabitCompleted(int habitId, int userId) {
        validateUserId(userId);
        if (habitId <= 0) {
            throw new ValidationException("Invalid habit ID");
        }
        return streakDAO.markHabitCompleted(habitId, userId);
    }

    private void validateUserId(int userId) {
        if (userId <= 0) {
            throw new ValidationException("Invalid user ID");
        }
    }
}
