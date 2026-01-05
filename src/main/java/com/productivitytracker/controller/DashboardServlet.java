package com.productivitytracker.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/DashboardServlet")
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. Get existing session (do NOT create new)
            HttpSession session = request.getSession(false);

            // 2. Authentication check
            if (session == null || session.getAttribute("userEmail") == null) {
                response.sendRedirect("jsp/login.jsp");
                return;
            }

            /*
             * 3. Prepare dashboard metrics
             * TEMPORARY static values
             * Later: fetch from Service / DAO layer
             */
            int productivity = 75;
            int tasksCompleted = 5;
            int totalTasks = 7;
            int habitsFollowed = 3;
            int activitiesLogged = 4;

            // 4. Bind data to request scope
            request.setAttribute("productivity", productivity);
            request.setAttribute("tasksCompleted", tasksCompleted);
            request.setAttribute("totalTasks", totalTasks);
            request.setAttribute("habitsFollowed", habitsFollowed);
            request.setAttribute("activitiesLogged", activitiesLogged);

            // 5. Forward to dashboard view
            request.getRequestDispatcher("jsp/index.jsp")
                   .forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred in DashboardServlet.");
            request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
        }
    }
}