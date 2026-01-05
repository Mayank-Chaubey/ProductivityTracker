package com.productivitytracker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.productivitytracker.model.Task;
import com.productivitytracker.util.DBConnection;
import com.productivitytracker.util.Logger;

public class TaskDAO {

    // ---------- INSERT TASK ----------
    public boolean addTask(int userId, String taskName, String priority) {

        String sql =
            "INSERT INTO tasks (user_id, name, priority, status, created_date) " +
            "VALUES (?, ?, ?, 'Pending', CURRENT_DATE)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, taskName);
            ps.setString(3, priority);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            Logger.logError("Failed to add task", e);
            return false;
        }
    }

    // ---------- MARK TASK AS COMPLETED ----------
    public boolean completeTask(int taskId, int userId) {

        String sql =
            "UPDATE tasks SET status = 'Completed' " +
            "WHERE id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, taskId);
            ps.setInt(2, userId);

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            Logger.logError("Failed to mark task as completed", e);
            return false;
        }
    }

    // ---------- FETCH USER TASKS ----------
    public List<Task> getTasks(int userId) {

        List<Task> tasks = new ArrayList<>();

        String sql =
            "SELECT id, name, priority, status, created_date " +
            "FROM tasks " +
            "WHERE user_id = ? " +
            "ORDER BY created_date DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(
                        new Task(
                            rs.getInt("id"),
                            userId,
                            rs.getString("name"),
                            rs.getString("priority"),
                            rs.getString("status"),
                            rs.getDate("created_date")
                        )
                    );
                }
            }

        } catch (SQLException e) {
            Logger.logError("Failed to fetch user tasks", e);
        }

        return tasks;
    }

    // ---------- COUNT COMPLETED TASKS ----------
    public int countCompletedTasks(int userId) {

        String sql =
            "SELECT COUNT(*) FROM tasks " +
            "WHERE user_id = ? AND status = 'Completed'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = execute(ps, userId)) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            Logger.logError("Failed to count completed tasks", e);
            return 0;
        }
    }

    // ---------- Helper ----------
    private ResultSet execute(PreparedStatement ps, int userId) throws SQLException {
        ps.setInt(1, userId);
        return ps.executeQuery();
    }
}