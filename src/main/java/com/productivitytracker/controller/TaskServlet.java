package com.productivitytracker.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/TaskServlet")
public class TaskServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. Session check
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userId") == null) {
                response.sendRedirect("jsp/login.jsp");
                return;
            }

            // 2. Read and validate form data
            String taskName = request.getParameter("taskName");
            String priority = request.getParameter("priority");

            if (taskName == null || priority == null ||
                taskName.trim().isEmpty() || priority.trim().isEmpty()) {

                response.sendRedirect("jsp/tasks.jsp");
                return;
            }

            taskName = taskName.trim();
            priority = priority.trim();

            // 3. Persist task to database
            int userId = (Integer) session.getAttribute("userId");
            com.productivitytracker.dao.TaskDAO taskDAO = new com.productivitytracker.dao.TaskDAO();
            boolean success = taskDAO.addTask(userId, taskName, priority);
            if (!success) {
                response.sendRedirect("jsp/tasks.jsp?error=save_failed");
                return;
            }

            // 4. Redirect (PRG pattern)
            response.sendRedirect("jsp/tasks.jsp");
        } catch (Exception ex) {
            ex.printStackTrace();
            response.sendRedirect("jsp/error.jsp");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. Session check
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userEmail") == null) {
                response.sendRedirect("jsp/login.jsp");
                return;
            }

            // 2. Safe default list
            int userId = (Integer) session.getAttribute("userId");

            com.productivitytracker.dao.TaskDAO taskDAO = new com.productivitytracker.dao.TaskDAO();
            List<com.productivitytracker.model.Task> tasks = taskDAO.getTasks(userId);

            request.setAttribute("tasks", tasks);

            // 3. Forward to JSP
            request.getRequestDispatcher("jsp/tasks.jsp")
                   .forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred in TaskServlet.");
            request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
        }
    }
}