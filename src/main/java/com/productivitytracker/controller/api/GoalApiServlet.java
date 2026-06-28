package com.productivitytracker.controller.api;

import com.productivitytracker.dto.GoalDTO;
import com.productivitytracker.service.GoalService;
import com.productivitytracker.util.JsonUtil;
import com.productivitytracker.util.SessionUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/goals")
public class GoalApiServlet extends HttpServlet {

    private final GoalService goalService = new GoalService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (!SessionUtil.isLoggedIn(session)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\":false,\"error\":{\"code\":\"AUTH_REQUIRED\",\"message\":\"Login required\"}}");
            return;
        }
        List<GoalDTO> goals = goalService.getGoals(SessionUtil.getUserId(session));
        StringBuilder json = new StringBuilder("{\"success\":true,\"total\":").append(goals.size()).append(",\"data\":[");
        for (int i = 0; i < goals.size(); i++) {
            GoalDTO goal = goals.get(i);
            if (i > 0) {
                json.append(',');
            }
            json.append("{\"id\":").append(goal.getId())
                    .append(",\"title\":").append(JsonUtil.quote(goal.getTitle()))
                    .append(",\"type\":").append(JsonUtil.quote(goal.getGoalType()))
                    .append(",\"target\":").append(goal.getTargetValue())
                    .append(",\"current\":").append(goal.getCurrentValue())
                    .append(",\"progress\":").append(goal.getProgressPercentage())
                    .append(",\"status\":").append(JsonUtil.quote(goal.getStatus()))
                    .append('}');
        }
        json.append("]}");
        response.setContentType("application/json");
        response.getWriter().write(json.toString());
    }
}
