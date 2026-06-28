package com.productivitytracker.controller;

import com.productivitytracker.service.JournalService;
import com.productivitytracker.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/JournalServlet")
public class JournalServlet extends HttpServlet {

    private final JournalService journalService = new JournalService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (!SessionUtil.isLoggedIn(session)) {
            response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
            return;
        }
        int userId = SessionUtil.getUserId(session);
        String query = request.getParameter("q");
        request.setAttribute("entries", journalService.getEntries(userId, query));
        request.setAttribute("q", query == null ? "" : query);
        request.getRequestDispatcher("/jsp/journal.jsp").forward(request, response);
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
        boolean success;
        if ("delete".equalsIgnoreCase(request.getParameter("action"))) {
            success = journalService.deleteEntry(parseLong(request.getParameter("entryId")), userId);
        } else {
            success = journalService.addEntry(userId, request.getParameter("title"), request.getParameter("content"),
                    request.getParameter("moodScore"), request.getParameter("entryDate"));
        }
        response.sendRedirect(request.getContextPath() + "/JournalServlet" + (success ? "?success=1" : "?error=1"));
    }

    private long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception ex) {
            return 0;
        }
    }
}
