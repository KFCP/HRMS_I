package com.example.controller;

import com.example.dao.UserDAO;
import com.example.dao.impl.UserDAOImpl;
import com.example.model.UserBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// @WebServlet("/user/*") // Annotation removed, mapping will be in web.xml
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    public void init() {
        userDAO = new UserDAOImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) {
            action = "/list"; // Default action
        }

        try {
            switch (action) {
                case "/add":
                    showNewUserForm(request, response);
                    break;
                case "/delete":
                    deleteUser(request, response); // Assuming ID is passed as query param for GET delete
                    break;
                case "/list":
                case "/manage": // Alias for listing users
                default:
                    listUsers(request, response);
                    break;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Invalid ID format for user operations.");
            listUsers(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) {
             response.sendRedirect(request.getContextPath() + "/user/list");
            return;
        }

        try {
            switch (action) {
                case "/add":
                    insertUser(request, response);
                    break;
                case "/delete":
                    deleteUser(request, response);
                    break;
                // Add cases for update if it becomes a requirement
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action for POST");
                    break;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Invalid data format provided for user.");
            // Potentially redirect to form or list with error
            response.sendRedirect(request.getContextPath() + "/user/list?error=InvalidInput");
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<UserBean> userList = userDAO.getAllUsers();
        request.setAttribute("userList", userList);
        request.getRequestDispatcher("/jsp/user_list.jsp").forward(request, response);
    }

    private void showNewUserForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/user_add.jsp").forward(request, response);
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Username and password are required.");
            showNewUserForm(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            showNewUserForm(request, response);
            return;
        }
        
        // Check if user already exists
        if (userDAO.getUserByUsername(username) != null) {
            request.setAttribute("errorMessage", "Username already exists. Please choose a different one.");
            showNewUserForm(request, response);
            return;
        }

        UserBean newUser = new UserBean(0, username, password); // ID is auto-generated
        userDAO.addUser(newUser);
        response.sendRedirect(request.getContextPath() + "/user/list?success=add");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            // Optional: Add a check to prevent deleting the currently logged-in user or a default admin
            userDAO.deleteUser(id);
            response.sendRedirect(request.getContextPath() + "/user/list?success=delete");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid user ID for deletion.");
            listUsers(request, response);
        }
    }
}
