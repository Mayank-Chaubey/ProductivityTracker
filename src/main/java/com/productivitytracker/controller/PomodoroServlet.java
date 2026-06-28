package com.productivitytracker.controller;

import com.productivitytracker.dto.PomodoroSummaryDTO;
import com.productivitytracker.service.PomodoroService;
import com.productivitytracker.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Handles pomodoro session logging and summary reads.
 */
@WebServlet("/PomodoroServlet")
public class PomodoroServlet extends HttpServlet {

    private final PomodoroService pomodoroService = new PomodoroService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!SessionUtil.isLoggedIn(session)) {
            if (wantsJson(request)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            }
            return;
        }

        int userId = SessionUtil.getUserId(session);
        PomodoroSummaryDTO summary = pomodoroService.getSummary(userId);

        if (!wantsJson(request)) {
            request.setAttribute("summary", summary);
            request.getRequestDispatcher("/jsp/focus.jsp").forward(request, response);
            return;
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"sessionsToday\":" + summary.getSessionsToday() +
                ",\"focusMinutesToday\":" + summary.getFocusMinutesToday() +
                ",\"breakMinutesToday\":" + summary.getBreakMinutesToday() +
                ",\"totalSessionsLifetime\":" + summary.getTotalSessionsLifetime() + "}");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!SessionUtil.isLoggedIn(session)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        int userId = SessionUtil.getUserId(session);
        int workMinutes = parseInt(request.getParameter("workMinutes"), 25);
        int breakMinutes = parseInt(request.getParameter("breakMinutes"), 5);
        int cycles = parseInt(request.getParameter("cycles"), 1);

        boolean logged = pomodoroService.logSession(userId, workMinutes, breakMinutes, cycles);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\":" + logged + "}");
    }

    private int parseInt(String value, int fallback) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return fallback;
        }
    }

    private boolean wantsJson(HttpServletRequest request) {
        String format = request.getParameter("format");
        String accept = request.getHeader("Accept");
        return "json".equalsIgnoreCase(format) || (accept != null && accept.contains("application/json"));
    }
}
