package com.productivitytracker.controller;

import com.productivitytracker.model.User;
import com.productivitytracker.service.AuthService;
import com.productivitytracker.service.EmailService;
import com.productivitytracker.util.Logger;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {

    private final AuthService authService = new AuthService();
    private final EmailService emailService = new EmailService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/forgot-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = safe(request.getParameter("email"));
        try {
            User user = authService.getUserByEmail(email);
            String token = authService.createPasswordResetToken(email);
            if (user != null && token != null) {
                emailService.sendPasswordResetEmail(user, token);
                if ("development".equalsIgnoreCase(com.productivitytracker.config.AppConfig.get("app.environment", "development"))) {
                    request.setAttribute("resetLink", request.getContextPath() + "/ResetPasswordServlet?token=" + token);
                }
            }
            request.setAttribute("successMessage", "If an account exists for that email, a reset link has been sent.");
            request.getRequestDispatcher("/jsp/forgot-password.jsp").forward(request, response);
        } catch (Exception ex) {
            Logger.logError("ForgotPasswordServlet error", ex);
            request.setAttribute("errorMessage", "Could not process reset request.");
            request.getRequestDispatcher("/jsp/forgot-password.jsp").forward(request, response);
        }
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
