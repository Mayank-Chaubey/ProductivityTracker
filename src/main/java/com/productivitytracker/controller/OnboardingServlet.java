package com.productivitytracker.controller;

import com.productivitytracker.dao.UserDAO;
import com.productivitytracker.model.UserSettings;
import com.productivitytracker.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/OnboardingServlet")
public class OnboardingServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/onboarding.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!SessionUtil.isLoggedIn(session)) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }
        int userId = SessionUtil.getUserId(session);
        UserSettings settings = userDAO.getSettings(userId);
        settings.setEmailNotifications("on".equalsIgnoreCase(request.getParameter("emailNotifications")));
        settings.setBrowserNotifications("on".equalsIgnoreCase(request.getParameter("browserNotifications")));
        userDAO.updateSettings(settings);
        userDAO.markOnboarded(userId);
        response.sendRedirect(request.getContextPath() + "/DashboardServlet?onboarded=true");
    }
}
