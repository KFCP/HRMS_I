package com.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// @WebServlet("/logout") // Annotation removed, mapping will be in web.xml
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false); // Get session if it exists, don't create new one
        if (session != null) {
            session.invalidate();
        }
        // Redirect to the login page
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp?logout=true");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // In case someone tries to POST to /logout, treat it the same as GET
        doGet(request, response);
    }
}
