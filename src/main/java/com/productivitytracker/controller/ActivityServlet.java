package com.productivitytracker.controller;

import java.io.IOException;
import java.util.regex.Pattern;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * ActivityServlet handles creation and retrieval of user activities.
 * Enforces enterprise-level security: session fixation protection, CSRF, input validation, and user scoping.
 */
@WebServlet("/ActivityServlet")
public class ActivityServlet extends HttpServlet {
    // Allow only letters, numbers, spaces, and basic punctuation in activity names
    private static final Pattern ACTIVITY_NAME_PATTERN = Pattern.compile("^[\\w\\s.,'\\-]{1,100}$");
    private static final int MAX_DURATION = 1440; // 24 hours in minutes

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
            String activityName = request.getParameter("activityName");
            String durationStr = request.getParameter("duration");
            if (activityName == null || durationStr == null ||
                !ACTIVITY_NAME_PATTERN.matcher(activityName.trim()).matches()) {
                response.sendRedirect("jsp/activities.jsp?error=invalid_input");
                return;
            }
            int duration;
            try {
                duration = Integer.parseInt(durationStr.trim());
                if (duration <= 0 || duration > MAX_DURATION) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("jsp/activities.jsp?error=invalid_duration");
                return;
            }

            // 4. Use ActivityService to persist activity
            com.productivitytracker.service.ActivityService activityService = new com.productivitytracker.service.ActivityService();
            boolean success = activityService.addActivity(userId, activityName.trim(), duration);
            if (!success) {
                response.sendRedirect("jsp/activities.jsp?error=save_failed");
                return;
            }

            // 5. Redirect (PRG pattern)
            response.sendRedirect("jsp/activities.jsp?success=1");
        } catch (Exception ex) {
            com.productivitytracker.util.Logger.logError("ActivityServlet POST error", ex);
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

            // 3. Get activities from DB via service
            com.productivitytracker.service.ActivityService activityService = new com.productivitytracker.service.ActivityService();
            java.util.List<com.productivitytracker.model.Activity> activities = activityService.getTodayActivities(userId);
            request.setAttribute("activities", activities);

            // 4. Forward to JSP
            request.getRequestDispatcher("jsp/activities.jsp").forward(request, response);
        } catch (Exception ex) {
            com.productivitytracker.util.Logger.logError("ActivityServlet GET error", ex);
            response.sendRedirect("jsp/error.jsp");
        }
    }
}