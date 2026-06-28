package com.productivitytracker.controller;

import com.productivitytracker.model.Reminder;
import com.productivitytracker.service.ReminderService;
import com.productivitytracker.util.JsonUtil;
import com.productivitytracker.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/ReminderServlet")
public class ReminderServlet extends HttpServlet {

    private final ReminderService reminderService = new ReminderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!SessionUtil.isLoggedIn(session)) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }
        int userId = SessionUtil.getUserId(session);
        if ("due".equalsIgnoreCase(request.getParameter("action"))) {
            writeDueJson(response, reminderService.getDueRemindersForUser(userId));
            return;
        }
        request.setAttribute("reminders", reminderService.getReminders(userId));
        request.getRequestDispatcher("/jsp/reminders.jsp").forward(request, response);
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
        if ("delete".equalsIgnoreCase(action)) {
            success = reminderService.deleteReminder(parseLong(request.getParameter("reminderId")), userId);
        } else if ("dismiss".equalsIgnoreCase(action)) {
            success = reminderService.dismissReminder(parseLong(request.getParameter("reminderId")), userId);
        } else {
            success = reminderService.addReminder(userId, request.getParameter("reminderType"),
                    request.getParameter("title"), request.getParameter("remindAt"), request.getParameter("channel"));
        }
        response.sendRedirect(request.getContextPath() + "/ReminderServlet" + (success ? "?success=1" : "?error=1"));
    }

    private void writeDueJson(HttpServletResponse response, List<Reminder> reminders) throws IOException {
        response.setContentType("application/json");
        StringBuilder json = new StringBuilder("{\"success\":true,\"data\":[");
        for (int i = 0; i < reminders.size(); i++) {
            Reminder reminder = reminders.get(i);
            if (i > 0) {
                json.append(',');
            }
            json.append("{\"id\":").append(reminder.getId())
                    .append(",\"title\":").append(JsonUtil.quote(reminder.getTitle()))
                    .append(",\"type\":").append(JsonUtil.quote(reminder.getReminderType()))
                    .append(",\"remindAt\":").append(JsonUtil.quote(String.valueOf(reminder.getRemindAt())))
                    .append('}');
        }
        json.append("]}");
        response.getWriter().write(json.toString());
    }

    private long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception ex) {
            return 0;
        }
    }
}
