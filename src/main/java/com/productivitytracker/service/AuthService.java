package com.productivitytracker.service;

import com.productivitytracker.dao.UserDAO;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    // ---------- LOGIN ----------
    public Integer authenticate(String email, String password) {

        if (email == null || password == null) {
            return null;
        }

        if (email.isEmpty() || password.isEmpty()) {
            return null;
        }

        // Validate credentials
        boolean valid = userDAO.validateUser(email, password);

        if (!valid) {
            return null;
        }

        // Fetch userId for session usage
        return userDAO.getUserIdByEmail(email);
    }

    // ---------- REGISTER ----------
    public boolean register(String name, String email, String password) {

        if (name == null || email == null || password == null) {
            return false;
        }

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            return false;
        }

        // Check if user already exists
        if (userDAO.userExists(email)) {
            return false;
        }

        // Save user (plain password for now)
        return userDAO.registerUser(name, email, password);
    }
}