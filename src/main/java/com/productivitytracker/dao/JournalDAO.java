package com.productivitytracker.dao;

import com.productivitytracker.model.JournalEntry;
import com.productivitytracker.util.DBConnection;
import com.productivitytracker.util.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JournalDAO {

    public boolean addEntry(int userId, String title, String content, Integer moodScore, Date entryDate) {
        String sql = "INSERT INTO journal_entries (user_id, title, content, mood_score, entry_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, title);
            ps.setString(3, content);
            if (moodScore == null) {
                ps.setNull(4, java.sql.Types.TINYINT);
            } else {
                ps.setInt(4, moodScore);
            }
            ps.setDate(5, entryDate);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            Logger.logError("Failed to add journal entry", e);
            return false;
        }
    }

    public List<JournalEntry> getEntries(int userId, String query) {
        List<JournalEntry> entries = new ArrayList<>();
        boolean hasQuery = query != null && !query.isBlank();
        String sql = "SELECT id, user_id, title, content, mood_score, entry_date, created_at FROM journal_entries " +
                "WHERE user_id = ?" + (hasQuery ? " AND (title LIKE ? OR content LIKE ?)" : "") +
                " ORDER BY entry_date DESC, created_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            if (hasQuery) {
                String pattern = "%" + query.trim() + "%";
                ps.setString(2, pattern);
                ps.setString(3, pattern);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    entries.add(new JournalEntry(
                            rs.getLong("id"),
                            rs.getInt("user_id"),
                            rs.getString("title"),
                            rs.getString("content"),
                            (Integer) rs.getObject("mood_score"),
                            rs.getDate("entry_date"),
                            rs.getTimestamp("created_at")
                    ));
                }
            }
        } catch (SQLException e) {
            Logger.logError("Failed to fetch journal entries", e);
        }

        return entries;
    }

    public boolean deleteEntry(long entryId, int userId) {
        String sql = "DELETE FROM journal_entries WHERE id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, entryId);
            ps.setInt(2, userId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            Logger.logError("Failed to delete journal entry", e);
            return false;
        }
    }
}
