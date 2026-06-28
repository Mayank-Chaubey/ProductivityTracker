package com.productivitytracker.controller;

import com.productivitytracker.util.DBConnection;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;

@WebServlet("/health")
public class HealthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        try (Connection connection = DBConnection.getConnection()) {
            if (connection.isValid(2)) {
                response.getWriter().write("{\"status\":\"ok\",\"database\":\"ok\"}");
                return;
            }
        } catch (Exception ignored) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
        response.getWriter().write("{\"status\":\"error\",\"database\":\"unavailable\"}");
    }
}
