package com.example.controller;

import com.example.dao.PositionDAO;
import com.example.dao.impl.PositionDAOImpl;
import com.example.model.PositionBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// @WebServlet("/position/*") // Annotation removed, mapping will be in web.xml
public class PositionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PositionDAO positionDAO;

    public void init() {
        positionDAO = new PositionDAOImpl();
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
                    showNewForm(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/delete":
                    deletePosition(request, response);
                    break;
                case "/list":
                case "/query": // Assuming query might be used for filtering, but basic list for now
                default:
                    listPositions(request, response);
                    break;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Invalid ID format.");
            listPositions(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
         if (action == null) {
            response.sendRedirect(request.getContextPath() + "/position/list");
            return;
        }

        try {
            switch (action) {
                case "/add":
                    insertPosition(request, response);
                    break;
                case "/edit":
                    updatePosition(request, response);
                    break;
                 case "/delete": // Allowing POST for delete as well
                    deletePosition(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action for POST");
                    break;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Invalid data format provided.");
            // Redirect to list or appropriate form with error
            response.sendRedirect(request.getContextPath() + "/position/list?error=InvalidInput");
        }
    }

    private void listPositions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<PositionBean> positionList = positionDAO.getAllPositions();
        request.setAttribute("positionList", positionList);
        request.getRequestDispatcher("/jsp/position_list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/position_add.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        PositionBean existingPosition = positionDAO.getPositionById(id);
        if (existingPosition != null) {
            request.setAttribute("position", existingPosition);
            request.getRequestDispatcher("/jsp/position_edit.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Position not found for ID: " + id);
            listPositions(request, response);
        }
    }

    private void insertPosition(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String positionName = request.getParameter("positionName");
            int level = Integer.parseInt(request.getParameter("level"));

            if (positionName == null || positionName.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Position Name is required.");
                showNewForm(request, response);
                return;
            }
            if (level <= 0) {
                request.setAttribute("errorMessage", "Level must be a positive number.");
                showNewForm(request, response);
                return;
            }


            PositionBean newPosition = new PositionBean(0, positionName, level); // ID is auto-generated
            positionDAO.addPosition(newPosition);
            response.sendRedirect(request.getContextPath() + "/position/list?success=add");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid number format for level.");
            showNewForm(request, response);
        }
    }

    private void updatePosition(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String positionName = request.getParameter("positionName");
            int level = Integer.parseInt(request.getParameter("level"));

            if (positionName == null || positionName.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Position Name is required.");
                PositionBean existingPosition = positionDAO.getPositionById(id);
                request.setAttribute("position", existingPosition);
                request.getRequestDispatcher("/jsp/position_edit.jsp").forward(request, response);
                return;
            }
            if (level <= 0) {
                request.setAttribute("errorMessage", "Level must be a positive number.");
                PositionBean existingPosition = positionDAO.getPositionById(id);
                request.setAttribute("position", existingPosition);
                request.getRequestDispatcher("/jsp/position_edit.jsp").forward(request, response);
                return;
            }

            PositionBean position = new PositionBean(id, positionName, level);
            positionDAO.updatePosition(position);
            response.sendRedirect(request.getContextPath() + "/position/list?success=update");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid number format for ID or level.");
             try {
                int id = Integer.parseInt(request.getParameter("id")); 
                PositionBean existingPosition = positionDAO.getPositionById(id);
                request.setAttribute("position", existingPosition);
            } catch (NumberFormatException nfe){
                // ignore if id was bad
            }
            request.getRequestDispatcher("/jsp/position_edit.jsp").forward(request, response);
        }
    }

    private void deletePosition(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            // Add check if position is in use by employees before deleting
            // For now, direct delete:
            positionDAO.deletePosition(id);
            response.sendRedirect(request.getContextPath() + "/position/list?success=delete");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid position ID for deletion.");
            listPositions(request, response);
        }
        // Consider adding a check here if any employee is associated with this position
        // If so, prevent deletion or show an error message.
        // For example: if (employeeDAO.countEmployeesByPosition(id) > 0) { /* show error */ }
    }
}
