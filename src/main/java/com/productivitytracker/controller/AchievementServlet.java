package com.productivitytracker.controller;

import com.productivitytracker.dto.AchievementDTO;
import com.productivitytracker.service.AchievementService;
import com.productivitytracker.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Displays badge progress and unlocked achievements.
 */
@WebServlet("/AchievementServlet")
public class AchievementServlet extends HttpServlet {

    private final AchievementService achievementService = new AchievementService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!SessionUtil.isLoggedIn(session)) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        int userId = SessionUtil.getUserId(session);
        List<AchievementDTO> achievements = achievementService.evaluateAndGetAchievements(userId);
        int unlockedCount = (int) achievements.stream().filter(AchievementDTO::isUnlocked).count();
        int totalPoints = achievements.stream()
                .filter(AchievementDTO::isUnlocked)
                .mapToInt(AchievementDTO::getPoints)
                .sum();

        request.setAttribute("achievements", achievements);
        request.setAttribute("unlockedCount", unlockedCount);
        request.setAttribute("lockedCount", achievements.size() - unlockedCount);
        request.setAttribute("totalPoints", totalPoints);
        request.getRequestDispatcher("/jsp/achievements.jsp").forward(request, response);
    }
}
