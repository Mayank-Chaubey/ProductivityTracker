package com.productivitytracker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.productivitytracker.util.DBConnection;
import com.productivitytracker.util.Logger;

public class ReportDAO {

    // ---------- TASK SUMMARY ----------
    public int getTotalTasks(int userId) {

        String sql = "SELECT COUNT(*) FROM tasks WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = execute(ps, userId)) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            Logger.logError("Failed to get total tasks", e);
            return 0;
        }
    }

    public int getCompletedTasks(int userId) {

        String sql =
            "SELECT COUNT(*) FROM tasks " +
            "WHERE user_id = ? AND status = 'Completed'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = execute(ps, userId)) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            Logger.logError("Failed to get completed tasks", e);
            return 0;
        }
    }

    // ---------- HABIT SUMMARY ----------
    public int getTotalHabits(int userId) {

        String sql = "SELECT COUNT(*) FROM habits WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = execute(ps, userId)) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            Logger.logError("Failed to get total habits", e);
            return 0;
        }
    }

    // ---------- ACTIVITY SUMMARY ----------
    public int getTodayActivitiesCount(int userId) {

        String sql =
            "SELECT COUNT(*) FROM activities " +
            "WHERE user_id = ? AND activity_date = CURRENT_DATE";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = execute(ps, userId)) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            Logger.logError("Failed to get today's activities count", e);
            return 0;
        }
    }

    // ---------- TIME SUMMARY ----------
    public int getTodayTotalTime(int userId) {

        String sql =
            "SELECT COALESCE(SUM(duration), 0) " +
            "FROM activities " +
            "WHERE user_id = ? AND activity_date = CURRENT_DATE";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = execute(ps, userId)) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            Logger.logError("Failed to get today's total time", e);
            return 0;
        }
    }

    // ---------- Helper ----------
    private ResultSet execute(PreparedStatement ps, int userId) throws SQLException {
        ps.setInt(1, userId);
        return ps.executeQuery();
    }
}