package com.productivitytracker.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. Get existing session (do not create new)
            HttpSession session = request.getSession(false);

            // 2. Invalidate session
            if (session != null) {
                session.invalidate();
            }

            // 3. Prevent caching of protected pages
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            // 4. Redirect to login controller/page
            response.sendRedirect("jsp/login.jsp");
            // or: response.sendRedirect("LoginServlet");
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred in LogoutServlet.");
            request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
        }
    }
}