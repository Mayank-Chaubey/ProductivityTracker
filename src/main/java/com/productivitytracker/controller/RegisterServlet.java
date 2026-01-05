package com.productivitytracker.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. Read and sanitize form data
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmPassword");

            if (name == null || email == null || password == null || confirmPassword == null ||
                name.trim().isEmpty() || email.trim().isEmpty() ||
                password.trim().isEmpty() || confirmPassword.trim().isEmpty()) {

                request.setAttribute("errorMessage", "All fields are required");
                request.getRequestDispatcher("jsp/register.jsp")
                       .forward(request, response);
                return;
            }

            name = name.trim();
            email = email.trim();
            password = password.trim();
            confirmPassword = confirmPassword.trim();

            // 2. Password match check
            if (!password.equals(confirmPassword)) {
                request.setAttribute("errorMessage", "Passwords do not match");
                request.getRequestDispatcher("jsp/register.jsp")
                       .forward(request, response);
                return;
            }

            // 3. Registration logic: persist user to database
            com.productivitytracker.dao.UserDAO userDAO = new com.productivitytracker.dao.UserDAO();
            boolean success = userDAO.registerUser(name, email, password);
            if (!success) {
                request.setAttribute("errorMessage", "Registration failed. Email may already be in use.");
                request.getRequestDispatcher("jsp/register.jsp")
                       .forward(request, response);
                return;
            }

            // 4. Redirect to login after successful registration
            response.sendRedirect("jsp/login.jsp");
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("errorMessage", "An unexpected error occurred in RegisterServlet.");
            request.getRequestDispatcher("jsp/error.jsp").forward(request, response);
        }
    }
}