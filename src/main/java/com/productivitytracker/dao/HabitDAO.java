package com.productivitytracker.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.productivitytracker.model.Habit;
import com.productivitytracker.util.DBConnection;
import com.productivitytracker.util.Logger;

public class HabitDAO {

    // ---------- ADD HABIT ----------
    public boolean addHabit(int userId, String habitName, String frequency) {

        String sql =
            "INSERT INTO habits (user_id, habit_name, frequency, streak, last_done_date) " +
            "VALUES (?, ?, ?, 0, NULL)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, habitName);
            ps.setString(3, frequency);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            Logger.logError("Failed to add habit", e);
            return false;
        }
    }

    // ---------- MARK HABIT AS DONE ----------
    public boolean markHabitDone(int habitId, int userId) {

        String sql =
            "UPDATE habits " +
            "SET streak = streak + 1, last_done_date = CURRENT_DATE " +
            "WHERE id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, habitId);
            ps.setInt(2, userId);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            Logger.logError("Failed to mark habit as done", e);
            return false;
        }
    }

    // ---------- FETCH USER HABITS ----------
    public List<Habit> getHabits(int userId) {

        List<Habit> habits = new ArrayList<>();

        String sql =
            "SELECT id, habit_name, frequency, streak, last_done_date " +
            "FROM habits " +
            "WHERE user_id = ? " +
            "ORDER BY habit_name";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    habits.add(
                        new Habit(
                            rs.getInt("id"),
                            userId,
                            rs.getString("habit_name"),
                            rs.getString("frequency"),
                            rs.getInt("streak"),
                            rs.getDate("last_done_date")
                        )
                    );
                }
            }

        } catch (SQLException e) {
            Logger.logError("Failed to fetch user habits", e);
        }

        return habits;
    }

    // ---------- COUNT HABITS ----------
    public int countHabits(int userId) {

        String sql =
            "SELECT COUNT(*) FROM habits WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {
            Logger.logError("Failed to count habits", e);
        }

        return 0;
    }
}