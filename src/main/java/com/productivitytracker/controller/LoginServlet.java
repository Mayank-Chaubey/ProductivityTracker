package com.productivitytracker.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. Read and sanitize form data
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            if (email == null || password == null ||
                email.trim().isEmpty() || password.trim().isEmpty()) {

                request.setAttribute("errorMessage", "Email and password are required");
                request.getRequestDispatcher("jsp/login.jsp")
                       .forward(request, response);
                return;
            }

            email = email.trim();
            password = password.trim();

            /*
             * 2. Authentication logic (TEMPORARY)
             * Later: AuthService + UserDAO
             */
            com.productivitytracker.dao.UserDAO userDAO = new com.productivitytracker.dao.UserDAO();
            boolean authenticated = userDAO.validateUser(email, password);
            Integer userId = null;
            if (authenticated) {
                userId = userDAO.getUserIdByEmail(email);
            }

            if (authenticated && userId != null) {

                // 3. Prevent session fixation
                HttpSession oldSession = request.getSession(false);
                if (oldSession != null) {
                    oldSession.invalidate();
                }

                // 4. Create fresh session
                HttpSession session = request.getSession(true);
                session.setAttribute("userEmail", email);
                session.setAttribute("userId", userId);

                // 5. Redirect to controller (not JSP)
                response.sendRedirect("jsp/index.jsp");

            } else {

                // 6. Authentication failed
                request.setAttribute("errorMessage", "Invalid email or password");
                request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred in LoginServlet.");
            request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
        }
    }
}