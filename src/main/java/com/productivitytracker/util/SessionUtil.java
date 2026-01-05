package com.productivitytracker.util;

import jakarta.servlet.http.HttpSession;
import java.util.Objects;

public final class SessionUtil {

    // ---------- CONSTANTS ----------
    public static final String USER_ID_KEY = "userId";

    private SessionUtil() {
        throw new AssertionError("SessionUtil should not be instantiated");
    }

    // ---------- AUTH CHECK ----------
    public static boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute(USER_ID_KEY) instanceof Integer;
    }

    // ---------- USER ID ----------
    public static int getUserId(HttpSession session) {

        if (session == null) {
            throw new IllegalStateException("Session is null");
        }

        Object userId = session.getAttribute(USER_ID_KEY);

        if (!(userId instanceof Integer)) {
            throw new IllegalStateException("User is not logged in");
        }

        return (Integer) userId;
    }

    // ---------- LOGIN ----------
    public static void login(HttpSession session, int userId) {

        Objects.requireNonNull(session, "session must not be null");

        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid userId");
        }

        session.setAttribute(USER_ID_KEY, userId);
    }

    // ---------- LOGOUT ----------
    public static void logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
    }
}