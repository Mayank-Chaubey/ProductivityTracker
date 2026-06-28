package com.productivitytracker.dao;

import com.productivitytracker.model.Goal;
import com.productivitytracker.util.DBConnection;
import com.productivitytracker.util.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for monthly/quarterly goals.
 */
public class GoalDAO {

    public boolean createGoal(int userId, String title, String goalType, int targetValue,
                              LocalDate startDate, LocalDate endDate) {
        String sql = "INSERT INTO goals (user_id, title, goal_type, target_value, current_value, status, start_date, end_date) " +
                "VALUES (?, ?, ?, ?, 0, 'ACTIVE', ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, title);
            ps.setString(3, goalType);
            ps.setInt(4, targetValue);
            ps.setDate(5, Date.valueOf(startDate));
            ps.setDate(6, Date.valueOf(endDate));
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            Logger.logError("Failed to create goal", ex);
            return false;
        }
    }

    public List<Goal> getGoals(int userId) {
        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT id, title, goal_type, target_value, current_value, status, start_date, end_date " +
                "FROM goals WHERE user_id = ? ORDER BY end_date ASC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    goals.add(mapGoal(userId, rs));
                }
            }
        } catch (SQLException ex) {
            Logger.logError("Failed to fetch goals", ex);
        }
        return goals;
    }

    public List<Goal> getActiveGoals(int userId) {
        List<Goal> goals = new ArrayList<>();
        String sql = "SELECT id, title, goal_type, target_value, current_value, status, start_date, end_date " +
                "FROM goals WHERE user_id = ? AND status = 'ACTIVE' ORDER BY end_date ASC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    goals.add(mapGoal(userId, rs));
                }
            }
        } catch (SQLException ex) {
            Logger.logError("Failed to fetch active goals", ex);
        }
        return goals;
    }

    public int countCompletedGoals(int userId) {
        String sql = "SELECT COUNT(*) FROM goals WHERE user_id = ? AND status = 'COMPLETED'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            Logger.logError("Failed to count completed goals", ex);
            return 0;
        }
    }

    public int countTotalGoals(int userId) {
        String sql = "SELECT COUNT(*) FROM goals WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            Logger.logError("Failed to count total goals", ex);
            return 0;
        }
    }

    public boolean updateProgress(int goalId, int userId, int increment) {
        String sql = "UPDATE goals " +
                "SET current_value = current_value + ?, " +
                "status = CASE WHEN (current_value + ?) >= target_value THEN 'COMPLETED' ELSE status END " +
                "WHERE id = ? AND user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, increment);
            ps.setInt(2, increment);
            ps.setInt(3, goalId);
            ps.setInt(4, userId);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            Logger.logError("Failed to update goal progress", ex);
            return false;
        }
    }

    private Goal mapGoal(int userId, ResultSet rs) throws SQLException {
        Date startDate = rs.getDate("start_date");
        Date endDate = rs.getDate("end_date");
        return new Goal(
                rs.getInt("id"),
                userId,
                rs.getString("title"),
                rs.getString("goal_type"),
                rs.getInt("target_value"),
                rs.getInt("current_value"),
                rs.getString("status"),
                startDate == null ? null : startDate.toLocalDate(),
                endDate == null ? null : endDate.toLocalDate()
        );
    }
}
