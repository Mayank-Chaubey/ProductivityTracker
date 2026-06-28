package com.productivitytracker.dao;

import com.productivitytracker.model.PomodoroSession;
import com.productivitytracker.util.DBConnection;
import com.productivitytracker.util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for pomodoro session logging.
 */
public class PomodoroDAO {

    public boolean logSession(int userId, int workMinutes, int breakMinutes, int cycles, int totalFocusMinutes) {
        String sql = "INSERT INTO pomodoro_sessions (user_id, work_minutes, break_minutes, cycles, total_focus_minutes, completed_at) " +
                "VALUES (?, ?, ?, ?, ?, NOW())";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, workMinutes);
            ps.setInt(3, breakMinutes);
            ps.setInt(4, cycles);
            ps.setInt(5, totalFocusMinutes);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            Logger.logError("Failed to log pomodoro session", ex);
            return false;
        }
    }

    public int getTodaySessionCount(int userId) {
        String sql = "SELECT COUNT(*) FROM pomodoro_sessions WHERE user_id = ? AND DATE(completed_at) = CURRENT_DATE";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            Logger.logError("Failed to count today pomodoro sessions", ex);
            return 0;
        }
    }

    public int getTodayFocusMinutes(int userId) {
        String sql = "SELECT COALESCE(SUM(total_focus_minutes), 0) FROM pomodoro_sessions WHERE user_id = ? AND DATE(completed_at) = CURRENT_DATE";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            Logger.logError("Failed to fetch today focus minutes", ex);
            return 0;
        }
    }

    public int getTodayBreakMinutes(int userId) {
        String sql = "SELECT COALESCE(SUM(break_minutes * cycles), 0) FROM pomodoro_sessions WHERE user_id = ? AND DATE(completed_at) = CURRENT_DATE";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            Logger.logError("Failed to fetch today break minutes", ex);
            return 0;
        }
    }

    public int getTotalSessionCount(int userId) {
        String sql = "SELECT COUNT(*) FROM pomodoro_sessions WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            Logger.logError("Failed to fetch total pomodoro sessions", ex);
            return 0;
        }
    }

    public int getFocusMinutesForDate(int userId, LocalDate date) {
        String sql = "SELECT COALESCE(SUM(total_focus_minutes), 0) FROM pomodoro_sessions WHERE user_id = ? AND DATE(completed_at) = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setDate(2, java.sql.Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            Logger.logError("Failed to fetch focus minutes for date", ex);
            return 0;
        }
    }

    public List<PomodoroSession> getRecentSessions(int userId, int limit) {
        List<PomodoroSession> sessions = new ArrayList<>();
        String sql = "SELECT id, work_minutes, break_minutes, cycles, total_focus_minutes, completed_at " +
                "FROM pomodoro_sessions WHERE user_id = ? ORDER BY completed_at DESC LIMIT ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, Math.max(1, Math.min(limit, 30)));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp("completed_at");
                    LocalDateTime completedAt = timestamp == null ? null : timestamp.toLocalDateTime();
                    sessions.add(new PomodoroSession(
                            rs.getInt("id"),
                            userId,
                            rs.getInt("work_minutes"),
                            rs.getInt("break_minutes"),
                            rs.getInt("cycles"),
                            rs.getInt("total_focus_minutes"),
                            completedAt
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.logError("Failed to fetch recent pomodoro sessions", ex);
        }
        return sessions;
    }

    public List<PomodoroSession> getSessionsBetween(int userId, LocalDate startDate, LocalDate endDate) {
        List<PomodoroSession> sessions = new ArrayList<>();
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            return sessions;
        }

        String sql = "SELECT id, work_minutes, break_minutes, cycles, total_focus_minutes, completed_at " +
                "FROM pomodoro_sessions WHERE user_id = ? AND completed_at >= ? AND completed_at < ? " +
                "ORDER BY completed_at DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setTimestamp(2, Timestamp.valueOf(startDate.atStartOfDay()));
            ps.setTimestamp(3, Timestamp.valueOf(endDate.plusDays(1).atStartOfDay()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp("completed_at");
                    LocalDateTime completedAt = timestamp == null ? null : timestamp.toLocalDateTime();
                    sessions.add(new PomodoroSession(
                            rs.getInt("id"),
                            userId,
                            rs.getInt("work_minutes"),
                            rs.getInt("break_minutes"),
                            rs.getInt("cycles"),
                            rs.getInt("total_focus_minutes"),
                            completedAt
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.logError("Failed to fetch pomodoro sessions between dates", ex);
        }
        return sessions;
    }
}
