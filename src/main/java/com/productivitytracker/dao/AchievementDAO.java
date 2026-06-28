package com.productivitytracker.dao;

import com.productivitytracker.model.Achievement;
import com.productivitytracker.util.DBConnection;
import com.productivitytracker.util.Logger;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * DAO for unlocked achievements.
 */
public class AchievementDAO {

    public Set<String> getUnlockedCodes(int userId) {
        Set<String> codes = new HashSet<>();
        String sql = "SELECT achievement_code FROM user_achievements WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    codes.add(rs.getString("achievement_code"));
                }
            }
        } catch (SQLException ex) {
            Logger.logError("Failed to fetch unlocked achievements", ex);
        }
        return codes;
    }

    public boolean unlock(int userId, String code) {
        String sql = "INSERT IGNORE INTO user_achievements (user_id, achievement_code, unlocked_on) VALUES (?, ?, CURRENT_DATE)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, code);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            Logger.logError("Failed to unlock achievement " + code, ex);
            return false;
        }
    }

    public int countUnlocked(int userId) {
        String sql = "SELECT COUNT(*) FROM user_achievements WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            Logger.logError("Failed to count unlocked achievements", ex);
            return 0;
        }
    }

    public List<Achievement> getRecentUnlocked(int userId, int limit) {
        List<Achievement> achievements = new ArrayList<>();
        String sql = "SELECT achievement_code, unlocked_on FROM user_achievements WHERE user_id = ? " +
                "ORDER BY unlocked_on DESC LIMIT ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, Math.max(1, Math.min(limit, 20)));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date unlockedOn = rs.getDate("unlocked_on");
                    achievements.add(new Achievement(
                            rs.getString("achievement_code"),
                            userId,
                            unlockedOn == null ? null : unlockedOn.toLocalDate()
                    ));
                }
            }
        } catch (SQLException ex) {
            Logger.logError("Failed to fetch recent unlocked achievements", ex);
        }
        return achievements;
    }
}
