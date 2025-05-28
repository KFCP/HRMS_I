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
import java.util.Arrays;
import java.util.List;

// Using @WebFilter annotation is an alternative to XML configuration,
// but the task asks to update web.xml, so this might be commented out or removed
// if web.xml is the sole source of truth for filter mapping.
// For this task, I will rely on web.xml for mapping.
// @WebFilter("/*") 
public class AuthenticationFilter implements Filter {

    private List<String> publicPaths;
    private List<String> publicPrefixes;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Define paths that don't require authentication
        publicPaths = Arrays.asList(
            "/jsp/user_login.jsp",
            "/login",  // Servlet path for LoginServlet
            "/logout"  // Servlet path for LogoutServlet
        );
        // Define path prefixes that don't require authentication
        publicPrefixes = Arrays.asList(
            "/css",
            "/js" // If you have JavaScript files in a /js directory
        );
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false); // Do not create session if it doesn't exist

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Check if the path is public
        boolean isPublicPath = publicPaths.contains(path);
        if (!isPublicPath) {
            for (String prefix : publicPrefixes) {
                if (path.startsWith(prefix)) {
                    isPublicPath = true;
                    break;
                }
            }
        }
        
        // Allow access to public paths
        if (isPublicPath) {
            chain.doFilter(request, response);
            return;
        }

        // Check for user in session if path is not public
        boolean loggedIn = (session != null && session.getAttribute("user") != null);

        if (loggedIn) {
            // User is logged in, allow access
            chain.doFilter(request, response);
        } else {
            // User is not logged in, redirect to login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/jsp/user_login.jsp?auth=required");
        }
    }

    @Override
    public void destroy() {
        // Cleanup resources if any
    }
}
