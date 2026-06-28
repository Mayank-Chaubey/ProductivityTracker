package com.productivitytracker.service;

import com.productivitytracker.dao.GoalDAO;
import com.productivitytracker.dto.GoalDTO;
import com.productivitytracker.exception.ValidationException;
import com.productivitytracker.model.Goal;
import com.productivitytracker.util.Logger;

import java.time.LocalDate;
import java.util.List;

/**
 * Service for monthly/quarterly goals.
 */
public class GoalService {

    private final GoalDAO goalDAO;

    public GoalService() {
        this.goalDAO = new GoalDAO();
    }

    public boolean createGoal(int userId, String title, String goalType, int targetValue) {
        try {
            validateUserId(userId);
            if (title == null || title.isBlank()) {
                throw new ValidationException("Goal title is required");
            }
            if (!"MONTHLY".equalsIgnoreCase(goalType) && !"QUARTERLY".equalsIgnoreCase(goalType)) {
                throw new ValidationException("Goal type must be MONTHLY or QUARTERLY");
            }
            if (targetValue <= 0) {
                throw new ValidationException("Target value must be greater than 0");
            }

            LocalDate startDate = LocalDate.now();
            LocalDate endDate = "QUARTERLY".equalsIgnoreCase(goalType) ? startDate.plusMonths(3) : startDate.plusMonths(1);
            return goalDAO.createGoal(userId, title.trim(), goalType.toUpperCase(), targetValue, startDate, endDate);
        } catch (RuntimeException ex) {
            Logger.logError("Service error in createGoal", ex);
            return false;
        }
    }

    public List<GoalDTO> getActiveGoals(int userId) {
        try {
            validateUserId(userId);
            return goalDAO.getActiveGoals(userId).stream()
                    .map(this::toDTO)
                    .toList();
        } catch (RuntimeException ex) {
            Logger.logError("Service error in getActiveGoals", ex);
            return List.of();
        }
    }

    public List<GoalDTO> getGoals(int userId) {
        try {
            validateUserId(userId);
            return goalDAO.getGoals(userId).stream()
                    .map(this::toDTO)
                    .toList();
        } catch (RuntimeException ex) {
            Logger.logError("Service error in getGoals", ex);
            return List.of();
        }
    }

    public int countCompletedGoals(int userId) {
        try {
            validateUserId(userId);
            return goalDAO.countCompletedGoals(userId);
        } catch (RuntimeException ex) {
            Logger.logError("Service error in countCompletedGoals", ex);
            return 0;
        }
    }

    public int countTotalGoals(int userId) {
        try {
            validateUserId(userId);
            return goalDAO.countTotalGoals(userId);
        } catch (RuntimeException ex) {
            Logger.logError("Service error in countTotalGoals", ex);
            return 0;
        }
    }

    public boolean incrementGoalProgress(int goalId, int userId, int increment) {
        try {
            validateUserId(userId);
            if (goalId <= 0 || increment <= 0) {
                throw new ValidationException("Invalid goal progress update");
            }
            return goalDAO.updateProgress(goalId, userId, increment);
        } catch (RuntimeException ex) {
            Logger.logError("Service error in incrementGoalProgress", ex);
            return false;
        }
    }

    private GoalDTO toDTO(Goal goal) {
        int progress = goal.getTargetValue() <= 0 ? 0 : Math.min(100, (goal.getCurrentValue() * 100) / goal.getTargetValue());
        return new GoalDTO(
                goal.getId(),
                goal.getTitle(),
                goal.getGoalType(),
                goal.getTargetValue(),
                goal.getCurrentValue(),
                progress,
                goal.getStatus(),
                goal.getStartDate(),
                goal.getEndDate()
        );
    }

    private void validateUserId(int userId) {
        if (userId <= 0) {
            throw new ValidationException("Invalid user ID");
        }
    }
}
