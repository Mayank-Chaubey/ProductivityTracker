package com.productivitytracker.dao;

import com.productivitytracker.model.Reminder;
import com.productivitytracker.util.DBConnection;
import com.productivitytracker.util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ReminderDAO {

    public boolean addReminder(int userId, String type, Integer referenceId, String title,
                               Timestamp remindAt, String channel) {
        String sql = "INSERT INTO reminders (user_id, reminder_type, reference_id, title, remind_at, channel, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, 'ACTIVE')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, type);
            if (referenceId == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, referenceId);
            }
            ps.setString(4, title);
            ps.setTimestamp(5, remindAt);
            ps.setString(6, channel);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            Logger.logError("Failed to add reminder", e);
            return false;
        }
    }

    public List<Reminder> getReminders(int userId) {
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT id, user_id, reminder_type, reference_id, title, remind_at, channel, status " +
                "FROM reminders WHERE user_id = ? ORDER BY remind_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reminders.add(map(rs));
                }
            }
        } catch (SQLException e) {
            Logger.logError("Failed to fetch reminders", e);
        }

        return reminders;
    }

    public List<Reminder> getDueReminders(int limit) {
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT id, user_id, reminder_type, reference_id, title, remind_at, channel, status " +
                "FROM reminders WHERE status = 'ACTIVE' AND remind_at <= NOW() " +
                "AND channel IN ('EMAIL', 'BOTH') AND sent_at IS NULL ORDER BY remind_at ASC LIMIT ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Math.max(1, Math.min(limit, 100)));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reminders.add(map(rs));
                }
            }
        } catch (SQLException e) {
            Logger.logError("Failed to fetch due reminders", e);
        }

        return reminders;
    }

    public List<Reminder> getDueRemindersForUser(int userId) {
        List<Reminder> reminders = new ArrayList<>();
        String sql = "SELECT id, user_id, reminder_type, reference_id, title, remind_at, channel, status " +
                "FROM reminders WHERE user_id = ? AND status = 'ACTIVE' AND remind_at <= NOW() " +
                "AND channel IN ('IN_APP', 'BOTH') ORDER BY remind_at ASC LIMIT 10";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reminders.add(map(rs));
                }
            }
        } catch (SQLException e) {
            Logger.logError("Failed to fetch user due reminders", e);
        }

        return reminders;
    }

    public boolean markEmailSent(long reminderId) {
        String sql = "UPDATE reminders SET sent_at = NOW() WHERE id = ?";
        return updateById(sql, reminderId);
    }

    public boolean dismissReminder(long reminderId, int userId) {
        String sql = "UPDATE reminders SET status = 'SENT' WHERE id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, reminderId);
            ps.setInt(2, userId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            Logger.logError("Failed to dismiss reminder", e);
            return false;
        }
    }

    public boolean deleteReminder(long reminderId, int userId) {
        String sql = "DELETE FROM reminders WHERE id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, reminderId);
            ps.setInt(2, userId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            Logger.logError("Failed to delete reminder", e);
            return false;
        }
    }

    private boolean updateById(String sql, long reminderId) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, reminderId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            Logger.logError("Failed to update reminder", e);
            return false;
        }
    }

    private Reminder map(ResultSet rs) throws SQLException {
        int referenceId = rs.getInt("reference_id");
        return new Reminder(
                rs.getLong("id"),
                rs.getInt("user_id"),
                rs.getString("reminder_type"),
                rs.wasNull() ? null : referenceId,
                rs.getString("title"),
                rs.getTimestamp("remind_at"),
                rs.getString("channel"),
                rs.getString("status")
        );
    }
}
