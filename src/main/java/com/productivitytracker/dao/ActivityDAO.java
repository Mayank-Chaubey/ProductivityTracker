package com.productivitytracker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.productivitytracker.model.Activity;
import com.productivitytracker.util.DBConnection;
import com.productivitytracker.util.Logger;

/**
 * DAO for Activity persistence. All methods are user-scoped and use parameterized queries.
 */
public class ActivityDAO {

    /**
     * Insert a new activity for a user.
     * @param userId the user ID
     * @param activityName the activity name
     * @param duration duration in minutes
     * @return true if inserted, false otherwise
     */
    public boolean addActivity(int userId, String activityName, int duration) {

        String sql =
            "INSERT INTO activities (user_id, activity_name, duration, activity_date) " +
            "VALUES (?, ?, ?, CURRENT_DATE)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, activityName);
            ps.setInt(3, duration);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            Logger.logError("Failed to add activity", e);
            return false;
        }
    }

    /**
     * Fetch today's activities for a user.
     * @param userId the user ID
     * @return list of Activity objects
     */
    public List<Activity> getTodayActivities(int userId) {

        List<Activity> activities = new ArrayList<>();

        String sql =
            "SELECT id, activity_name, duration, activity_date " +
            "FROM activities " +
            "WHERE user_id = ? AND activity_date = CURRENT_DATE";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    activities.add(
                        new Activity(
                            rs.getInt("id"),
                            userId,
                            rs.getString("activity_name"),
                            rs.getInt("duration"),
                            rs.getDate("activity_date")
                        )
                    );
                }
            }

        } catch (SQLException e) {
            Logger.logError("Failed to fetch today's activities", e);
        }

        return activities;
    }

    /**
     * Fetch all activities for a user.
     * @param userId the user ID
     * @return list of Activity objects
     */
    public List<Activity> getAllActivities(int userId) {

        List<Activity> activities = new ArrayList<>();

        String sql =
            "SELECT id, activity_name, duration, activity_date " +
            "FROM activities " +
            "WHERE user_id = ? " +
            "ORDER BY activity_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    activities.add(
                        new Activity(
                            rs.getInt("id"),
                            userId,
                            rs.getString("activity_name"),
                            rs.getInt("duration"),
                            rs.getDate("activity_date")
                        )
                    );
                }
            }

        } catch (SQLException e) {
            Logger.logError("Failed to fetch all activities", e);
        }

        return activities;
    }
}