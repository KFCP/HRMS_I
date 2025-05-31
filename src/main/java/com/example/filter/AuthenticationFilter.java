package com.example.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// Filter configuration is in web.xml, so @WebFilter annotation is not strictly needed here
// and can be removed if web.xml is the sole source of truth.
public class AuthenticationFilter implements Filter {

    private Set<String> excludedPaths;
    private Set<String> excludedPrefixes;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Define paths that don't require authentication (exact matches)
        // These are relative to the context path.
        excludedPaths = new HashSet<>(Arrays.asList(
            "/",        // Root path, handled by LoginController
            "/login"    // Login action, handled by LoginController
            // Logout is not strictly needed here as it requires a session to invalidate,
            // but if accessed directly without session, it will redirect to login anyway.
            // If logout page itself had public resources, that'd be different.
        ));

        // Define path prefixes that don't require authentication
        excludedPrefixes = new HashSet<>(Arrays.asList(
            "/css/",
            "/js/",
            "/img/"
            // "/jsp/user_login.jsp" // Direct JSP access should ideally be prevented by placing JSPs in WEB-INF
                                 // However, if it's accessed, it should be public.
                                 // But since /login controller path serves it, direct JSP access is not the primary flow.
        ));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        if (path.isEmpty()) { // Handle context root explicitly if it's not already "/"
            path = "/";
        }

        boolean isExcluded = excludedPaths.contains(path);
        if (!isExcluded) {
            for (String prefix : excludedPrefixes) {
                if (path.startsWith(prefix)) {
                    isExcluded = true;
                    break;
                }
            }
        }
        
        if (isExcluded) {
            chain.doFilter(request, response); // Bypass authentication
        } else {
            HttpSession session = httpRequest.getSession(false); // Do not create session if it doesn't exist
            // Check for "loggedInUser" attribute set by LoginController
            if (session != null && session.getAttribute("loggedInUser") != null) {
                chain.doFilter(request, response); // User is logged in, allow access
            } else {
                // User is not logged in, redirect to the /login controller path
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?auth=required");
            }
        }
    }

    @Override
    public void destroy() {
        // Cleanup resources if any
    }
}
