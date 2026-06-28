package com.productivitytracker.filter;

import com.productivitytracker.dao.UserDAO;
import com.productivitytracker.model.User;
import com.productivitytracker.util.SessionUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

/**
 * Protects authenticated pages and servlets from anonymous access.
 */
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    private static final String REMEMBER_COOKIE = "pt_remember";
    private final UserDAO userDAO = new UserDAO();

    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/",
            "/index.html",
            "/LoginServlet",
            "/RegisterServlet",
            "/ForgotPasswordServlet",
            "/ResetPasswordServlet",
            "/EmailVerificationServlet",
            "/health",
            "/jsp/forgot-password.jsp",
            "/jsp/reset-password.jsp",
            "/jsp/email-verification.jsp",
            "/jsp/help.jsp",
            "/jsp/privacy.jsp",
            "/jsp/terms.jsp",
            "/jsp/login.jsp",
            "/jsp/register.jsp",
            "/jsp/error.jsp",
            "/manifest.json"
    );

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String contextPath = request.getContextPath();
        String path = request.getRequestURI().substring(contextPath.length());

        if (isPublic(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = request.getSession(false);
        if (!SessionUtil.isLoggedIn(session)) {
            if (!restoreRememberedSession(request)) {
                if (path.startsWith("/api/")) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"success\":false,\"error\":{\"code\":\"AUTH_REQUIRED\",\"message\":\"Login required\"}}");
                    return;
                }
                response.sendRedirect(contextPath + "/jsp/login.jsp?expired=true");
                return;
            }
            session = request.getSession(false);
        }

        if (!isCurrentSessionVersion(session)) {
            if (session != null) {
                session.invalidate();
            }
            clearRememberCookie(response, contextPath);
            response.sendRedirect(contextPath + "/jsp/login.jsp?expired=true");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean restoreRememberedSession(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return false;
        }
        for (Cookie cookie : cookies) {
            if (REMEMBER_COOKIE.equals(cookie.getName())) {
                Integer userId = userDAO.findUserIdByRememberMeToken(cookie.getValue());
                if (userId == null) {
                    return false;
                }
                User user = userDAO.getUserById(userId);
                if (user == null) {
                    return false;
                }
                HttpSession session = request.getSession(true);
                SessionUtil.login(session, userId, user.getSessionVersion());
                session.setAttribute("userEmail", user.getEmail());
                return true;
            }
        }
        return false;
    }

    private boolean isCurrentSessionVersion(HttpSession session) {
        if (!SessionUtil.isLoggedIn(session)) {
            return false;
        }
        int userId = SessionUtil.getUserId(session);
        Object version = session.getAttribute(SessionUtil.SESSION_VERSION_KEY);
        int sessionVersion = version instanceof Integer ? (Integer) version : 0;
        return sessionVersion == userDAO.getSessionVersion(userId);
    }

    private void clearRememberCookie(HttpServletResponse response, String contextPath) {
        Cookie cookie = new Cookie(REMEMBER_COOKIE, "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath(contextPath == null || contextPath.isBlank() ? "/" : contextPath);
        response.addCookie(cookie);
    }

    private boolean isPublic(String path) {
        return PUBLIC_PATHS.contains(path)
                || path.startsWith("/assets/")
                || path.startsWith("/META-INF/")
                || path.endsWith(".css")
                || path.endsWith(".js")
                || path.endsWith(".svg")
                || path.endsWith(".png")
                || path.endsWith(".ico");
    }
}
