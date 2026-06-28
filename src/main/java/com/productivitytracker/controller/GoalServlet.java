package com.productivitytracker.controller;

import com.productivitytracker.service.GoalService;
import com.productivitytracker.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles monthly/quarterly goal creation.
 */
@WebServlet("/GoalServlet")
public class GoalServlet extends HttpServlet {

    private final GoalService goalService = new GoalService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!SessionUtil.isLoggedIn(session)) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        int userId = SessionUtil.getUserId(session);
        request.setAttribute("goals", goalService.getGoals(userId));
        request.setAttribute("activeGoalsCount", goalService.getActiveGoals(userId).size());
        request.setAttribute("completedGoalsCount", goalService.countCompletedGoals(userId));
        request.setAttribute("totalGoalsCount", goalService.countTotalGoals(userId));
        request.getRequestDispatcher("/jsp/goals.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!SessionUtil.isLoggedIn(session)) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        int userId = SessionUtil.getUserId(session);
        String action = request.getParameter("action");
        boolean success;

        if ("increment".equalsIgnoreCase(action)) {
            int goalId = parseInt(request.getParameter("goalId"), 0);
            int increment = parseInt(request.getParameter("increment"), 1);
            success = goalService.incrementGoalProgress(goalId, userId, increment);
        } else {
            String title = request.getParameter("title");
            String goalType = request.getParameter("goalType");
            int targetValue = parseInt(request.getParameter("targetValue"), 0);
            success = goalService.createGoal(userId, title, goalType, targetValue);
        }

        String returnTo = safeReturnTo(request.getParameter("returnTo"));
        String statusParam = success ? "?goal=updated" : "?goal=failed";
        if (returnTo.contains("?")) {
            statusParam = success ? "&goal=updated" : "&goal=failed";
        }
        response.sendRedirect(request.getContextPath() + returnTo + statusParam);
    }

    private int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return fallback;
        }
    }

    private String safeReturnTo(String value) {
        if (value == null || value.isBlank() || !value.startsWith("/")) {
            return "/GoalServlet";
        }
        if (value.contains("://") || value.startsWith("//")) {
            return "/GoalServlet";
        }
        return value;
    }
}
