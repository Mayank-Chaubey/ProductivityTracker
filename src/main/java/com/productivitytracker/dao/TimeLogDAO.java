package com.productivitytracker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.productivitytracker.model.TimeLog;
import com.productivitytracker.util.DBConnection;

public class TimeLogDAO {

    // ---------- ADD TIME LOG ----------
    public boolean addTimeLog(int userId,
                              String referenceType,
                              int referenceId,
                              int durationMinutes) {

        String sql =
            "INSERT INTO time_logs (user_id, reference_type, reference_id, duration_minutes, log_date) " +
            "VALUES (?, ?, ?, ?, CURRENT_DATE)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, referenceType);
            ps.setInt(3, referenceId);
            ps.setInt(4, durationMinutes);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ---------- FETCH TODAY'S TIME LOGS ----------
    public List<TimeLog> getTodayLogs(int userId) {

        List<TimeLog> logs = new ArrayList<>();

        String sql =
            "SELECT id, reference_type, reference_id, duration_minutes, log_date " +
            "FROM time_logs " +
            "WHERE user_id = ? AND log_date = CURRENT_DATE";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    logs.add(
                        new TimeLog(
                            rs.getInt("id"),
                            userId,
                            rs.getString("reference_type"),
                            rs.getInt("reference_id"),
                            rs.getInt("duration_minutes"),
                            rs.getDate("log_date")
                        )
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logs;
    }

    // ---------- TOTAL TIME FOR TODAY ----------
    public int getTotalTimeForToday(int userId) {

        String sql =
            "SELECT COALESCE(SUM(duration_minutes), 0) " +
            "FROM time_logs " +
            "WHERE user_id = ? AND log_date = CURRENT_DATE";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = execute(ps, userId)) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ---------- Helper ----------
    private ResultSet execute(PreparedStatement ps, int userId) throws SQLException {
        ps.setInt(1, userId);
        return ps.executeQuery();
    }
}