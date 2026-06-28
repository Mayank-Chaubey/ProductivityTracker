package com.productivitytracker.service;

import com.productivitytracker.dao.AchievementDAO;
import com.productivitytracker.dao.GoalDAO;
import com.productivitytracker.dao.PomodoroDAO;
import com.productivitytracker.dao.ReportDAO;
import com.productivitytracker.dao.StreakDAO;
import com.productivitytracker.dto.AchievementDTO;
import com.productivitytracker.exception.ValidationException;
import com.productivitytracker.model.Achievement;
import com.productivitytracker.util.Logger;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

/**
 * Service for achievements/badges evaluation and retrieval.
 */
public class AchievementService {

    private final AchievementDAO achievementDAO;
    private final ReportDAO reportDAO;
    private final StreakDAO streakDAO;
    private final PomodoroDAO pomodoroDAO;
    private final GoalDAO goalDAO;

    public AchievementService() {
        this.achievementDAO = new AchievementDAO();
        this.reportDAO = new ReportDAO();
        this.streakDAO = new StreakDAO();
        this.pomodoroDAO = new PomodoroDAO();
        this.goalDAO = new GoalDAO();
    }

    public List<AchievementDTO> evaluateAndGetAchievements(int userId) {
        try {
            validateUserId(userId);
            Set<String> unlockedCodes = achievementDAO.getUnlockedCodes(userId);

            List<Rule> rules = defaultRules(userId);
            for (Rule rule : rules) {
                if (!unlockedCodes.contains(rule.code()) && rule.metric().getAsInt() >= rule.threshold()) {
                    achievementDAO.unlock(userId, rule.code());
                }
            }

            Set<String> refreshedUnlockedCodes = achievementDAO.getUnlockedCodes(userId);
            Map<String, LocalDate> unlockedOnMap = achievementDAO.getRecentUnlocked(userId, 100).stream()
                    .collect(Collectors.toMap(Achievement::getCode, Achievement::getUnlockedOn, (first, second) -> first));

            List<AchievementDTO> dtoList = new ArrayList<>();
            for (Rule rule : rules) {
                boolean unlocked = refreshedUnlockedCodes.contains(rule.code());
                dtoList.add(new AchievementDTO(
                        rule.code(),
                        rule.title(),
                        rule.description(),
                        rule.icon(),
                        rule.points(),
                        unlocked,
                        unlocked ? unlockedOnMap.get(rule.code()) : null
                ));
            }

            return dtoList.stream()
                    .sorted(Comparator.comparing(AchievementDTO::isUnlocked).reversed()
                            .thenComparing(AchievementDTO::getPoints).reversed())
                    .toList();
        } catch (RuntimeException ex) {
            Logger.logError("Service error in evaluateAndGetAchievements", ex);
            return List.of();
        }
    }

    public int countUnlockedAchievements(int userId) {
        try {
            validateUserId(userId);
            return achievementDAO.countUnlocked(userId);
        } catch (RuntimeException ex) {
            Logger.logError("Service error in countUnlockedAchievements", ex);
            return 0;
        }
    }

    private List<Rule> defaultRules(int userId) {
        return List.of(
                new Rule("FIRST_TASK", "First Win", "Complete your first task", "🏁", 20,
                        () -> reportDAO.getCompletedTasks(userId), 1),
                new Rule("STREAK_7", "Consistency", "Reach a 7-day streak", "🔥", 60,
                        () -> streakDAO.getBestStreak(userId), 7),
                new Rule("FOCUS_120", "Deep Focus", "Log 120 focus minutes in a day", "🧠", 50,
                        () -> pomodoroDAO.getTodayFocusMinutes(userId), 120),
                new Rule("POMODORO_10", "Pomodoro Pro", "Complete 10 pomodoro sessions", "🍅", 70,
                        () -> pomodoroDAO.getTotalSessionCount(userId), 10),
                new Rule("GOAL_MASTER", "Goal Getter", "Complete your first goal", "🎯", 80,
                        () -> goalDAO.countCompletedGoals(userId), 1),
                new Rule("PRODUCTIVITY_80", "High Performer", "Maintain 80% productivity", "📈", 90,
                        () -> {
                            int total = reportDAO.getTotalTasks(userId);
                            if (total <= 0) {
                                return 0;
                            }
                            return (reportDAO.getCompletedTasks(userId) * 100) / total;
                        }, 80)
        );
    }

    private void validateUserId(int userId) {
        if (userId <= 0) {
            throw new ValidationException("Invalid user ID");
        }
    }

    private record Rule(String code, String title, String description, String icon, int points,
                        IntSupplier metric, int threshold) {
    }
}
