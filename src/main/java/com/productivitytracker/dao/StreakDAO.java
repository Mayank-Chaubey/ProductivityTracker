package com.productivitytracker.dao;

import com.productivitytracker.model.Streak;
import com.productivitytracker.util.DBConnection;
import com.productivitytracker.util.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for habit streak reads and updates.
 */
public class StreakDAO {

    public List<Streak> getStreaksForUser(int userId) {
        List<Streak> streaks = new ArrayList<>();
        String sql = "SELECT h.id, h.user_id, h.habit_name, h.streak AS habit_streak, h.last_done_date, " +
                "hs.weekly_streak, hs.current_streak, hs.longest_streak, hs.freeze_tokens, hs.last_completed_on " +
                "FROM habits h LEFT JOIN habit_streaks hs ON hs.habit_id = h.id " +
                "WHERE h.user_id = ? ORDER BY COALESCE(hs.current_streak, h.streak) DESC, h.habit_name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int currentStreak = rs.getInt("current_streak");
                    if (rs.wasNull()) {
                        currentStreak = rs.getInt("habit_streak");
                    }

                    int weeklyStreak = rs.getInt("weekly_streak");
                    int longestStreak = rs.getInt("longest_streak");
                    int freezeTokens = rs.getInt("freeze_tokens");
                    if (rs.wasNull()) {
                        freezeTokens = 0;
                    }

                    Date lastCompletedOn = rs.getDate("last_completed_on");
                    if (lastCompletedOn == null) {
                        lastCompletedOn = rs.getDate("last_done_date");
                    }

                    streaks.add(new Streak(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("habit_name"),
                            Math.max(weeklyStreak, 0),
                            currentStreak,
                            Math.max(longestStreak, currentStreak),
                            freezeTokens,
                            lastCompletedOn
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.logError("Failed to fetch streaks, falling back to habits table", ex);
            return getFallbackStreaks(userId);
        }

        return streaks;
    }

    public int getBestStreak(int userId) {
        String sql = "SELECT COALESCE(MAX(longest_streak), 0) FROM habit_streaks WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            Logger.logError("Failed to fetch best streak from habit_streaks, fallback to habits", ex);
            return getBestStreakFallback(userId);
        }
    }

    public int getWeeklyBestStreak(int userId) {
        String sql = "SELECT COALESCE(MAX(weekly_streak), 0) FROM habit_streaks WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            Logger.logError("Failed to fetch weekly streak", ex);
            return 0;
        }
    }

    public int getTotalFreezeTokens(int userId) {
        String sql = "SELECT COALESCE(SUM(freeze_tokens), 0) FROM habit_streaks WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            Logger.logError("Failed to fetch freeze tokens", ex);
            return 0;
        }
    }

    public boolean markHabitCompleted(int habitId, int userId) {
        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            if (!habitExists(con, habitId, userId)) {
                con.rollback();
                return false;
            }

            ensureStreakRow(con, habitId, userId);

            if (alreadyCompletedToday(con, habitId, userId)) {
                con.commit();
                return true;
            }

            String selectSql = "SELECT current_streak, longest_streak, weekly_streak, freeze_tokens, last_completed_on " +
                    "FROM habit_streaks WHERE habit_id = ? AND user_id = ? FOR UPDATE";
            try (PreparedStatement select = con.prepareStatement(selectSql)) {
                select.setInt(1, habitId);
                select.setInt(2, userId);
                try (ResultSet rs = select.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }

                    int current = rs.getInt("current_streak");
                    int longest = rs.getInt("longest_streak");
                    int weekly = rs.getInt("weekly_streak");
                    int freezeTokens = rs.getInt("freeze_tokens");
                    Date lastCompletedDateRaw = rs.getDate("last_completed_on");
                    LocalDate lastCompletedDate = lastCompletedDateRaw == null ? null : lastCompletedDateRaw.toLocalDate();
                    LocalDate today = LocalDate.now();

                    int nextCurrent = resolveNextCurrentStreak(current, lastCompletedDate, today, freezeTokens);
                    boolean freezeUsed = didUseFreeze(current, nextCurrent, lastCompletedDate, today, freezeTokens);
                    int nextFreezeTokens = freezeUsed ? Math.max(0, freezeTokens - 1) : freezeTokens;
                    if (nextCurrent > 0 && nextCurrent % 7 == 0 && nextFreezeTokens < 5) {
                        nextFreezeTokens += 1;
                    }

                    int nextWeekly = resolveNextWeeklyStreak(weekly, lastCompletedDate, today, freezeUsed);
                    int nextLongest = Math.max(longest, nextCurrent);

                    updateStreakRow(con, habitId, userId, nextCurrent, nextLongest, nextWeekly, nextFreezeTokens, today);
                    updateHabitFallbackColumns(con, habitId, userId, nextCurrent, today);
                    insertCompletionLog(con, habitId, userId, freezeUsed, today);
                }
            }

            con.commit();
            return true;
        } catch (SQLException ex) {
            Logger.logError("Failed to update streak", ex);
            return markHabitCompletedFallback(habitId, userId);
        }
    }

    private void updateStreakRow(Connection con, int habitId, int userId, int currentStreak, int longestStreak,
                                 int weeklyStreak, int freezeTokens, LocalDate completedOn) throws SQLException {
        String sql = "UPDATE habit_streaks SET current_streak = ?, longest_streak = ?, weekly_streak = ?, freeze_tokens = ?, " +
                "last_completed_on = ?, updated_at = NOW() WHERE habit_id = ? AND user_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, currentStreak);
            ps.setInt(2, longestStreak);
            ps.setInt(3, weeklyStreak);
            ps.setInt(4, freezeTokens);
            ps.setDate(5, Date.valueOf(completedOn));
            ps.setInt(6, habitId);
            ps.setInt(7, userId);
            ps.executeUpdate();
        }
    }

    private void updateHabitFallbackColumns(Connection con, int habitId, int userId, int currentStreak,
                                            LocalDate completedOn) throws SQLException {
        String sql = "UPDATE habits SET streak = ?, last_done_date = ? WHERE id = ? AND user_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, currentStreak);
            ps.setDate(2, Date.valueOf(completedOn));
            ps.setInt(3, habitId);
            ps.setInt(4, userId);
            ps.executeUpdate();
        }
    }

    private void insertCompletionLog(Connection con, int habitId, int userId, boolean freezeUsed, LocalDate completedOn) throws SQLException {
        String sql = "INSERT INTO habit_completion_log (user_id, habit_id, completed_on, freeze_used) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, habitId);
            ps.setDate(3, Date.valueOf(completedOn));
            ps.setBoolean(4, freezeUsed);
            ps.executeUpdate();
        }
    }

    private int resolveNextCurrentStreak(int current, LocalDate lastCompletedDate, LocalDate today, int freezeTokens) {
        if (lastCompletedDate == null) {
            return 1;
        }
        long days = ChronoUnit.DAYS.between(lastCompletedDate, today);
        if (days <= 0) {
            return current;
        }
        if (days == 1) {
            return current + 1;
        }
        if (days == 2 && freezeTokens > 0) {
            return current + 1;
        }
        return 1;
    }

    private boolean didUseFreeze(int current, int nextCurrent, LocalDate lastCompletedDate, LocalDate today, int freezeTokens) {
        if (lastCompletedDate == null || freezeTokens <= 0) {
            return false;
        }
        long days = ChronoUnit.DAYS.between(lastCompletedDate, today);
        return days == 2 && nextCurrent == current + 1;
    }

    private int resolveNextWeeklyStreak(int weekly, LocalDate lastCompletedDate, LocalDate today, boolean freezeUsed) {
        if (lastCompletedDate == null) {
            return 1;
        }

        LocalDate previousWeekStart = lastCompletedDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate currentWeekStart = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        long days = ChronoUnit.DAYS.between(lastCompletedDate, today);
        boolean sameWeek = previousWeekStart.equals(currentWeekStart);

        if (sameWeek && (days == 1 || freezeUsed || days == 0)) {
            return weekly + 1;
        }
        return 1;
    }

    private boolean habitExists(Connection con, int habitId, int userId) throws SQLException {
        String sql = "SELECT 1 FROM habits WHERE id = ? AND user_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, habitId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void ensureStreakRow(Connection con, int habitId, int userId) throws SQLException {
        String sql = "INSERT INTO habit_streaks (habit_id, user_id, current_streak, longest_streak, weekly_streak, freeze_tokens, last_completed_on, created_at, updated_at) " +
                "VALUES (?, ?, 0, 0, 0, 2, NULL, NOW(), NOW()) " +
                "ON DUPLICATE KEY UPDATE updated_at = updated_at";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, habitId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    private boolean alreadyCompletedToday(Connection con, int habitId, int userId) throws SQLException {
        String sql = "SELECT 1 FROM habit_completion_log WHERE habit_id = ? AND user_id = ? AND completed_on = CURRENT_DATE";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, habitId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private int getBestStreakFallback(int userId) {
        String sql = "SELECT COALESCE(MAX(streak), 0) FROM habits WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            Logger.logError("Fallback best streak query failed", ex);
            return 0;
        }
    }

    private List<Streak> getFallbackStreaks(int userId) {
        List<Streak> streaks = new ArrayList<>();
        String sql = "SELECT id, user_id, habit_name, streak, last_done_date FROM habits WHERE user_id = ? ORDER BY streak DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int current = rs.getInt("streak");
                    streaks.add(new Streak(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("habit_name"),
                            current,
                            current,
                            current,
                            0,
                            rs.getDate("last_done_date")
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.logError("Fallback streak query failed", ex);
        }
        return streaks;
    }

    private boolean markHabitCompletedFallback(int habitId, int userId) {
        String selectSql = "SELECT streak, last_done_date FROM habits WHERE id = ? AND user_id = ?";
        String updateSql = "UPDATE habits SET streak = ?, last_done_date = CURRENT_DATE WHERE id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement select = con.prepareStatement(selectSql);
             PreparedStatement update = con.prepareStatement(updateSql)) {

            select.setInt(1, habitId);
            select.setInt(2, userId);

            int nextStreak;
            try (ResultSet rs = select.executeQuery()) {
                if (!rs.next()) {
                    return false;
                }

                Date lastDoneDate = rs.getDate("last_done_date");
                LocalDate today = LocalDate.now();
                if (lastDoneDate != null && today.equals(lastDoneDate.toLocalDate())) {
                    return true;
                }

                if (lastDoneDate != null && today.minusDays(1).equals(lastDoneDate.toLocalDate())) {
                    nextStreak = rs.getInt("streak") + 1;
                } else {
                    nextStreak = 1;
                }
            }

            update.setInt(1, nextStreak);
            update.setInt(2, habitId);
            update.setInt(3, userId);
            return update.executeUpdate() == 1;
        } catch (SQLException ex) {
            Logger.logError("Fallback failed to update streak", ex);
            return false;
        }
    }
}
