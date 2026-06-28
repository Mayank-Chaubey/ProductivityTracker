package com.productivitytracker.filter;

import com.productivitytracker.util.Logger;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Global exception boundary for unhandled servlet/filter errors.
 */
@WebFilter("/*")
public class GlobalExceptionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception ex) {
            Logger.logError("Unhandled application exception", ex);
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpRequest.setAttribute("errorMessage", "Unexpected server error. Please retry.");
            if (!httpResponse.isCommitted()) {
                httpRequest.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
            }
        }
    }
}
