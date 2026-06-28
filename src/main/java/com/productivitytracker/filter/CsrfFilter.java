package com.productivitytracker.filter;

import com.productivitytracker.util.CsrfTokenUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

/**
 * Enforces CSRF protection for state-changing requests.
 */
@WebFilter("/*")
public class CsrfFilter implements Filter {

    private static final Set<String> EXEMPT_PATHS = Set.of(
            "/assets/",
            "/manifest.json",
            "/index.html",
            "/LoginServlet",
            "/RegisterServlet"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        String method = httpRequest.getMethod();

        if (isExempt(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(true);
        String token = CsrfTokenUtil.getOrCreateToken(session);
        httpRequest.setAttribute("csrfToken", token);

        if ("POST".equalsIgnoreCase(method) && !CsrfTokenUtil.isValid(httpRequest, session)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF token");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isExempt(String path) {
        if (path == null) {
            return true;
        }
        if (path.startsWith("/assets/")) {
            return true;
        }
        for (String exempt : EXEMPT_PATHS) {
            if (path.equals(exempt)) {
                return true;
            }
        }
        return path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".svg")
                || path.endsWith(".png") || path.endsWith(".ico") || path.endsWith(".jpg")
                || path.endsWith(".jpeg") || path.endsWith(".gif");
    }
}
