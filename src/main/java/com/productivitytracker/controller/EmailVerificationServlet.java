package com.productivitytracker.controller;

import com.productivitytracker.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/EmailVerificationServlet")
public class EmailVerificationServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        boolean verified = authService.verifyEmail(request.getParameter("token"));
        request.setAttribute("verified", verified);
        request.getRequestDispatcher("/jsp/email-verification.jsp").forward(request, response);
    }
}
