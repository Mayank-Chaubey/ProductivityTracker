package com.productivitytracker.controller;

import com.productivitytracker.dao.UserDAO;
import com.productivitytracker.model.User;
import com.productivitytracker.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        User user = userDAO.getUserById(userId);
        if (user != null) {
            request.setAttribute("profileName", user.getName());
            request.setAttribute("profileEmail", user.getEmail());
        }

        request.getRequestDispatcher("/jsp/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String name = safe(request.getParameter("name"));
        String email = safe(request.getParameter("email"));
        String newPassword = safe(request.getParameter("password"));
        String currentPassword = safe(request.getParameter("currentPassword"));

        if (!ValidationUtil.isValidName(name)) {
            forwardWithError(request, response, "Please enter a valid name.");
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            forwardWithError(request, response, "Please enter a valid email.");
            return;
        }

        if (userDAO.emailExistsForOtherUser(userId, email)) {
            forwardWithError(request, response, "This email is already in use.");
            return;
        }

        if (!newPassword.isEmpty() && !ValidationUtil.isValidPassword(newPassword)) {
            forwardWithError(request, response, "Password must be at least 8 characters.");
            return;
        }

        if (!newPassword.isEmpty() && !userDAO.verifyPassword(userId, currentPassword)) {
            forwardWithError(request, response, "Current password is required to change your password.");
            return;
        }

        if (!userDAO.updateProfile(userId, name, email, newPassword)) {
            forwardWithError(request, response, "Could not update profile. Please try again.");
            return;
        }

        session.setAttribute("userEmail", email);
        response.sendRedirect(request.getContextPath() + "/ProfileServlet?success=true");
    }

    private void forwardWithError(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {

        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("profileName", safe(request.getParameter("name")));
        request.setAttribute("profileEmail", safe(request.getParameter("email")));
        request.getRequestDispatcher("/jsp/profile.jsp").forward(request, response);
    }

    private String safe(String value) {
        return value == null ? "" : value.trim();
    }
}
