package com.example.controller;

import com.example.dao.UserDAO;
import com.example.dao.impl.UserDAOImpl;
import com.example.model.UserBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// @WebServlet("/login") // Annotation removed, mapping will be in web.xml
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to the login page
        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Username and password are required.");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            return;
        }

        if (userDAO.isValidUser(username, password)) {
            UserBean user = userDAO.getUserByUsername(username); // Fetch user details
            HttpSession session = request.getSession();
            session.setAttribute("user", user); // Store UserBean or just username
            session.setAttribute("username", username); // Storing username for easier access
            
            // Redirect to a main page or dashboard
            response.sendRedirect(request.getContextPath() + "/jsp/index.jsp");
        } else {
            request.setAttribute("errorMessage", "Invalid username or password.");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        }
    }
}
