package com.productivitytracker.controller.api;

import com.productivitytracker.model.Task;
import com.productivitytracker.service.TaskService;
import com.productivitytracker.service.TaskServiceImpl;
import com.productivitytracker.util.JsonUtil;
import com.productivitytracker.util.SessionUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/tasks")
public class TaskApiServlet extends HttpServlet {

    private final TaskService taskService = new TaskServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (!SessionUtil.isLoggedIn(session)) {
            writeError(response, HttpServletResponse.SC_UNAUTHORIZED, "AUTH_REQUIRED", "Login required");
            return;
        }
        int userId = SessionUtil.getUserId(session);
        List<Task> tasks = taskService.getTasks(userId, request.getParameter("q"), request.getParameter("status"),
                request.getParameter("priority"), request.getParameter("tag"), request.getParameter("sort"));
        int page = parseInt(request.getParameter("page"), 1);
        int size = Math.min(parseInt(request.getParameter("size"), 25), 100);
        int from = Math.min((page - 1) * size, tasks.size());
        int to = Math.min(from + size, tasks.size());

        StringBuilder json = new StringBuilder("{\"success\":true,\"page\":").append(page)
                .append(",\"size\":").append(size)
                .append(",\"total\":").append(tasks.size())
                .append(",\"data\":[");
        for (int i = from; i < to; i++) {
            Task task = tasks.get(i);
            if (i > from) {
                json.append(',');
            }
            json.append("{\"id\":").append(task.getId())
                    .append(",\"name\":").append(JsonUtil.quote(task.getName()))
                    .append(",\"priority\":").append(JsonUtil.quote(task.getPriority()))
                    .append(",\"status\":").append(JsonUtil.quote(task.getStatus()))
                    .append(",\"dueDate\":").append(JsonUtil.quote(task.getDueDate() == null ? "" : task.getDueDate().toString()))
                    .append(",\"tags\":").append(JsonUtil.quote(task.getTags()))
                    .append('}');
        }
        json.append("]}");
        response.setContentType("application/json");
        response.getWriter().write(json.toString());
    }

    private int parseInt(String value, int fallback) {
        try {
            return Math.max(1, Integer.parseInt(value));
        } catch (Exception ex) {
            return fallback;
        }
    }

    private void writeError(HttpServletResponse response, int status, String code, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"success\":false,\"error\":{\"code\":" + JsonUtil.quote(code)
                + ",\"message\":" + JsonUtil.quote(message) + "}}");
    }
}
