package com.productivitytracker.controller;

import com.productivitytracker.dao.UserDAO;
import com.productivitytracker.model.User;
import com.productivitytracker.model.UserSettings;
import com.productivitytracker.service.AuthService;
import com.productivitytracker.service.EmailService;
import com.productivitytracker.util.Logger;
import com.productivitytracker.util.SessionUtil;
import com.productivitytracker.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/SettingsServlet")
public class SettingsServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final AuthService authService = new AuthService();
    private final EmailService emailService = new EmailService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!SessionUtil.isLoggedIn(session)) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }
        int userId = SessionUtil.getUserId(session);
        request.setAttribute("user", userDAO.getUserById(userId));
        request.setAttribute("settings", userDAO.getSettings(userId));
        request.setAttribute("securityEvents", userDAO.getSecurityEvents(userId, 10));
        request.getRequestDispatcher("/jsp/settings.jsp").forward(request, response);
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
        String action = safe(request.getParameter("action"));
        boolean success = false;
        String event = "settings_update";

        try {
            switch (action) {
                case "preferences":
                    success = updatePreferences(userId, request);
                    break;
                case "password":
                    success = authService.changePassword(userId, request.getParameter("currentPassword"), request.getParameter("newPassword"));
                    event = success ? "password_changed" : "password_change_failed";
                    break;
                case "logoutAll":
                    int version = userDAO.incrementSessionVersion(userId);
                    userDAO.deleteRememberMeTokens(userId);
                    session.setAttribute(SessionUtil.SESSION_VERSION_KEY, version);
                    success = true;
                    event = "logout_all_devices";
                    break;
                case "deleteAccount":
                    success = deleteAccount(userId, request, session);
                    event = success ? "account_deleted" : "account_delete_failed";
                    if (success) {
                        response.sendRedirect(request.getContextPath() + "/index.html?account=deleted");
                        return;
                    }
                    break;
                case "sendVerification":
                    success = sendVerification(userId);
                    event = "email_verification_sent";
                    break;
                default:
                    success = false;
            }
        } catch (Exception ex) {
            Logger.logError("SettingsServlet POST error", ex);
            success = false;
        }

        userDAO.logSecurityEvent(userId, event, action, request.getRemoteAddr(), request.getHeader("User-Agent"));
        response.sendRedirect(request.getContextPath() + "/SettingsServlet" + (success ? "?success=" + action : "?error=" + action));
    }

    private boolean updatePreferences(int userId, HttpServletRequest request) {
        UserSettings settings = new UserSettings();
        settings.setUserId(userId);
        settings.setEmailNotifications("on".equalsIgnoreCase(request.getParameter("emailNotifications")));
        settings.setBrowserNotifications("on".equalsIgnoreCase(request.getParameter("browserNotifications")));
        settings.setReminderLeadMinutes(parseInt(request.getParameter("reminderLeadMinutes"), 10, 0, 1440));
        settings.setTimezone(safe(request.getParameter("timezone")).isBlank() ? "UTC" : safe(request.getParameter("timezone")));
        return userDAO.updateSettings(settings);
    }

    private boolean deleteAccount(int userId, HttpServletRequest request, HttpSession session) {
        String confirmation = safe(request.getParameter("deleteConfirm"));
        String currentPassword = safe(request.getParameter("deletePassword"));
        if (!"DELETE".equals(confirmation) || !userDAO.verifyPassword(userId, currentPassword)) {
            return false;
        }
        boolean deleted = userDAO.deleteAccount(userId);
        if (deleted) {
            session.invalidate();
        }
        return deleted;
    }

    private boolean sendVerification(int userId) {
        User user = userDAO.getUserById(userId);
        if (user == null || user.isEmailVerified()) {
            return false;
        }
        String token = authService.createEmailVerificationToken(userId);
        if (token == null) {
            return false;
        }
        emailService.sendEmailVerification(user, token);
        return true;
    }

    private int parseInt(String value, int fallback, int min, int max) {
        try {
            int parsed = Integer.parseInt(value);
            return Math.max(min, Math.min(max, parsed));
        } catch (Exception ex) {
            return fallback;
        }
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
