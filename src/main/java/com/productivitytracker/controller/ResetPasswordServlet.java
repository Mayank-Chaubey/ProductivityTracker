package com.productivitytracker.controller;

import com.productivitytracker.service.AuthService;
import com.productivitytracker.util.Logger;
import com.productivitytracker.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("token", safe(request.getParameter("token")));
        request.getRequestDispatcher("/jsp/reset-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = safe(request.getParameter("token"));
        String password = safe(request.getParameter("password"));
        String confirmPassword = safe(request.getParameter("confirmPassword"));

        try {
            if (!ValidationUtil.isValidPassword(password) || !ValidationUtil.passwordsMatch(password, confirmPassword)) {
                request.setAttribute("token", token);
                request.setAttribute("errorMessage", "Use a valid password and make sure both passwords match.");
                request.getRequestDispatcher("/jsp/reset-password.jsp").forward(request, response);
                return;
            }

            if (!authService.resetPassword(token, password)) {
                request.setAttribute("token", token);
                request.setAttribute("errorMessage", "Reset link is invalid or expired.");
                request.getRequestDispatcher("/jsp/reset-password.jsp").forward(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp?reset=success");
        } catch (Exception ex) {
            Logger.logError("ResetPasswordServlet error", ex);
            request.setAttribute("errorMessage", "Could not reset password.");
            request.getRequestDispatcher("/jsp/reset-password.jsp").forward(request, response);
        }
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
