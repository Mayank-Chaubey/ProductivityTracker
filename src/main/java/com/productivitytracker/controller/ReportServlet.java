package com.productivitytracker.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ReportServlet")
public class ReportServlet extends HttpServlet {

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

            /*
             * 2. Fetch data from session (TEMPORARY)
             * Later: replace with ReportService + DAO
             */
            @SuppressWarnings("unchecked")
            List<String> tasks =
                    (List<String>) session.getAttribute("tasks");

            @SuppressWarnings("unchecked")
            List<String> habits =
                    (List<String>) session.getAttribute("habits");

            @SuppressWarnings("unchecked")
            List<String> activities =
                    (List<String>) session.getAttribute("activities");

            if (tasks == null) tasks = new ArrayList<>();
            if (habits == null) habits = new ArrayList<>();
            if (activities == null) activities = new ArrayList<>();

            // 3. Task metrics
            int totalTasks = tasks.size();
            int completedTasks = 0;

            for (String task : tasks) {
                // TEMP logic — replace with Task.status later
                if (task.toLowerCase().contains("completed")) {
                    completedTasks++;
                }
            }

            // 4. Habit & activity metrics
            int totalHabits = habits.size();
            int totalActivities = activities.size();

            // 5. Productivity calculation (simple, transparent)
            int productivity = 0;
            if (totalTasks > 0) {
                productivity = (completedTasks * 100) / totalTasks;
            }

            // 6. Habit consistency (demo metric)
            int habitConsistency = (totalHabits == 0) ? 0 : 70; // placeholder %

            // 7. Time invested (demo metric)
            int totalTime = totalActivities * 60; // placeholder minutes

            /*
             * 8. Bind report attributes
             */
            request.setAttribute("totalTasks", totalTasks);
            request.setAttribute("completedTasks", completedTasks);
            request.setAttribute("totalHabits", totalHabits);
            request.setAttribute("totalActivities", totalActivities);
            request.setAttribute("productivity", productivity);
            request.setAttribute("habitConsistency", habitConsistency);
            request.setAttribute("totalTime", totalTime);

            // 9. Forward to reports view
            request.getRequestDispatcher("jsp/reports.jsp")
                   .forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred in ReportServlet.");
            request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
        }
    }
}