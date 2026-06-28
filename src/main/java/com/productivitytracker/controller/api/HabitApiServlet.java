package com.productivitytracker.controller.api;

import com.productivitytracker.model.Habit;
import com.productivitytracker.service.HabitService;
import com.productivitytracker.util.JsonUtil;
import com.productivitytracker.util.SessionUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/habits")
public class HabitApiServlet extends HttpServlet {

    private final HabitService habitService = new HabitService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (!SessionUtil.isLoggedIn(session)) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "AUTH_REQUIRED", "Login required");
            return;
        }
        List<Habit> habits = habitService.getHabits(SessionUtil.getUserId(session),
                request.getParameter("q"), request.getParameter("category"), request.getParameter("sort"));
        StringBuilder json = new StringBuilder("{\"success\":true,\"total\":").append(habits.size()).append(",\"data\":[");
        for (int i = 0; i < habits.size(); i++) {
            Habit habit = habits.get(i);
            if (i > 0) {
                json.append(',');
            }
            json.append("{\"id\":").append(habit.getId())
                    .append(",\"name\":").append(JsonUtil.quote(habit.getName()))
                    .append(",\"frequency\":").append(JsonUtil.quote(habit.getFrequency()))
                    .append(",\"frequencyRule\":").append(JsonUtil.quote(habit.getFrequencyRule()))
                    .append(",\"category\":").append(JsonUtil.quote(habit.getCategory()))
                    .append(",\"streak\":").append(habit.getStreak())
                    .append('}');
        }
        json.append("]}");
        response.setContentType("application/json");
        response.getWriter().write(json.toString());
    }

    private void writeError(HttpServletResponse response, int status, String code, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"success\":false,\"error\":{\"code\":" + JsonUtil.quote(code)
                + ",\"message\":" + JsonUtil.quote(message) + "}}");
    }
}
