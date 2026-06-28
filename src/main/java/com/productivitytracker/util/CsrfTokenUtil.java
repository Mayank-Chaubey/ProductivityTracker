package com.productivitytracker.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Objects;
import java.util.UUID;

/**
 * Utility for CSRF token lifecycle.
 */
public final class CsrfTokenUtil {

    public static final String CSRF_SESSION_KEY = "csrfToken";

    private CsrfTokenUtil() {
        throw new AssertionError("CsrfTokenUtil should not be instantiated");
    }

    public static String getOrCreateToken(HttpSession session) {
        Objects.requireNonNull(session, "session must not be null");
        Object token = session.getAttribute(CSRF_SESSION_KEY);
        if (token instanceof String && !((String) token).isBlank()) {
            return (String) token;
        }
        String newToken = UUID.randomUUID().toString();
        session.setAttribute(CSRF_SESSION_KEY, newToken);
        return newToken;
    }

    public static boolean isValid(HttpServletRequest request, HttpSession session) {
        if (request == null || session == null) {
            return false;
        }
        String sessionToken = (String) session.getAttribute(CSRF_SESSION_KEY);
        if (sessionToken == null || sessionToken.isBlank()) {
            return false;
        }
        String paramToken = request.getParameter("csrfToken");
        if (sessionToken.equals(paramToken)) {
            return true;
        }
        String headerToken = request.getHeader("X-CSRF-Token");
        return sessionToken.equals(headerToken);
    }
}
