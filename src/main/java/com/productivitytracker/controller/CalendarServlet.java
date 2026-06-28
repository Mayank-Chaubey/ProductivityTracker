package com.productivitytracker.controller;

import com.productivitytracker.dto.CalendarViewDTO;
import com.productivitytracker.service.CalendarService;
import com.productivitytracker.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.YearMonth;

/**
 * Displays dated tasks, goals, focus sessions, and due-soon reminders.
 */
@WebServlet("/CalendarServlet")
public class CalendarServlet extends HttpServlet {

    private final CalendarService calendarService = new CalendarService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!SessionUtil.isLoggedIn(session)) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        int userId = SessionUtil.getUserId(session);
        CalendarViewDTO calendar = calendarService.getMonthView(userId, parseMonth(request.getParameter("month")));
        request.setAttribute("calendar", calendar);
        request.getRequestDispatcher("/jsp/calendar.jsp").forward(request, response);
    }

    private YearMonth parseMonth(String monthValue) {
        try {
            return monthValue == null || monthValue.isBlank() ? YearMonth.now() : YearMonth.parse(monthValue);
        } catch (RuntimeException ex) {
            return YearMonth.now();
        }
    }
}
