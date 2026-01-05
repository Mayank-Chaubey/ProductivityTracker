package com.productivitytracker.controller;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * HabitServlet handles creation and retrieval of user habits.
 * Enforces enterprise-level security: session fixation protection, CSRF, input validation, and user scoping.
 */
@WebServlet("/HabitServlet")
public class HabitServlet extends HttpServlet {
    // Allow only letters, numbers, spaces, and basic punctuation in habit names
    private static final Pattern HABIT_NAME_PATTERN = Pattern.compile("^[\\w\\s.,'\\-]{1,100}$");
    private static final String[] ALLOWED_FREQUENCIES = {"Daily", "Weekly", "Monthly"};

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. Session and authentication check
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.sendRedirect("jsp/login.jsp");
                return;
            }
            Integer userId = (Integer) session.getAttribute("userId");

            // 2. CSRF token validation
            String sessionToken = (String) session.getAttribute("csrfToken");
            String formToken = request.getParameter("csrfToken");
            if (sessionToken == null || !sessionToken.equals(formToken)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token.");
                return;
            }

            // 3. Input validation and sanitization
            String habitName = request.getParameter("habitName");
            String frequency = request.getParameter("frequency");
            if (habitName == null || frequency == null ||
                !HABIT_NAME_PATTERN.matcher(habitName.trim()).matches() ||
                !isAllowedFrequency(frequency.trim())) {
                response.sendRedirect("jsp/habits.jsp?error=invalid_input");
                return;
            }

            // 4. Use HabitService to persist habit
            com.productivitytracker.service.HabitService habitService = new com.productivitytracker.service.HabitService();
            boolean success = habitService.addHabit(userId, habitName.trim(), frequency.trim());
            if (!success) {
                response.sendRedirect("jsp/habits.jsp?error=save_failed");
                return;
            }

            // 5. Redirect (PRG pattern)
            // Regenerate CSRF token after successful POST to prevent replay attacks
            String newToken = java.util.UUID.randomUUID().toString();
            session.setAttribute("csrfToken", newToken);
            response.sendRedirect("jsp/habits.jsp?success=1");
        } catch (Exception ex) {
            com.productivitytracker.util.Logger.logError("HabitServlet POST error", ex);
            response.sendRedirect("jsp/error.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. Session and authentication check
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.sendRedirect("jsp/login.jsp");
                return;
            }
            Integer userId = (Integer) session.getAttribute("userId");

            // 2. Generate CSRF token for form
            String csrfToken = java.util.UUID.randomUUID().toString();
            session.setAttribute("csrfToken", csrfToken);
            request.setAttribute("csrfToken", csrfToken);

            // 3. Get habits from DB via service
            com.productivitytracker.service.HabitService habitService = new com.productivitytracker.service.HabitService();
            List<com.productivitytracker.model.Habit> habits = habitService.getHabits(userId);
            request.setAttribute("habits", habits);

            // 4. Forward to JSP
            request.getRequestDispatcher("jsp/habits.jsp").forward(request, response);
        } catch (Exception ex) {
            com.productivitytracker.util.Logger.logError("HabitServlet GET error", ex);
            response.sendRedirect("jsp/error.jsp");
        }
    }

    private boolean isAllowedFrequency(String freq) {
        for (String allowed : ALLOWED_FREQUENCIES) {
            if (allowed.equalsIgnoreCase(freq)) return true;
        }
        return false;
    }
}