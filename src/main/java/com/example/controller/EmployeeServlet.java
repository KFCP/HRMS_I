package com.example.controller;

import com.example.dao.EmployeeDAO;
import com.example.dao.PositionDAO;
import com.example.dao.impl.EmployeeDAOImpl;
import com.example.dao.impl.PositionDAOImpl;
import com.example.model.EmployeeBean;
import com.example.model.PositionBean;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

// @WebServlet("/employee/*") // Annotation removed, mapping will be in web.xml
public class EmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private EmployeeDAO employeeDAO;
    private PositionDAO positionDAO;

    public void init() {
        employeeDAO = new EmployeeDAOImpl();
        positionDAO = new PositionDAOImpl(); // For fetching position list for dropdowns
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
                    deleteEmployee(request, response);
                    break;
                case "/list":
                case "/query":
                default:
                    listEmployees(request, response);
                    break;
            }
        } catch (NumberFormatException e) {
            // Log the error and redirect to an error page or list view with an error message
            e.printStackTrace(); // Consider a robust logging mechanism
            request.setAttribute("errorMessage", "Invalid ID format.");
            listEmployees(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if (action == null) {
            // Should not happen with POST, but handle defensively
            response.sendRedirect(request.getContextPath() + "/employee/list");
            return;
        }

        try {
            switch (action) {
                case "/add":
                    insertEmployee(request, response);
                    break;
                case "/edit":
                    updateEmployee(request, response);
                    break;
                case "/delete": // Allowing POST for delete as well
                    deleteEmployee(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action for POST");
                    break;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Handle error, perhaps redirect to form with error message
            request.setAttribute("errorMessage", "Invalid data format provided.");
            // Determine which form to show based on context or redirect to a general error page
            // For simplicity, redirecting to list, but a real app might go back to the form
            response.sendRedirect(request.getContextPath() + "/employee/list?error=InvalidInput");
        }
    }

    private void listEmployees(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchName = request.getParameter("searchName");
        List<EmployeeBean> employeeList;
        if (searchName != null && !searchName.trim().isEmpty()) {
            employeeList = employeeDAO.getEmployeesByName(searchName);
            request.setAttribute("searchName", searchName);
        } else {
            employeeList = employeeDAO.getAllEmployees();
        }
        request.setAttribute("employeeList", employeeList);
        request.getRequestDispatcher("/jsp/employee_list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<PositionBean> positionList = positionDAO.getAllPositions();
        request.setAttribute("positionList", positionList);
        request.getRequestDispatcher("/jsp/employee_add.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        EmployeeBean existingEmployee = employeeDAO.getEmployeeById(id);
        List<PositionBean> positionList = positionDAO.getAllPositions();
        if (existingEmployee != null) {
            request.setAttribute("employee", existingEmployee);
            request.setAttribute("positionList", positionList);
            request.getRequestDispatcher("/jsp/employee_edit.jsp").forward(request, response);
        } else {
            request.setAttribute("errorMessage", "Employee not found for ID: " + id);
            listEmployees(request, response);
        }
    }

    private void insertEmployee(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String name = request.getParameter("name");
            String gender = request.getParameter("gender");
            int age = Integer.parseInt(request.getParameter("age"));
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            int positionId = Integer.parseInt(request.getParameter("positionId"));

            if (name == null || name.trim().isEmpty() || gender == null || gender.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() || email == null || email.trim().isEmpty()) {
                request.setAttribute("errorMessage", "All fields are required.");
                showNewForm(request, response); // Show form again with error
                return;
            }
            if (age <= 0) {
                 request.setAttribute("errorMessage", "Age must be a positive number.");
                 showNewForm(request, response);
                 return;
            }

            EmployeeBean newEmployee = new EmployeeBean(0, name, gender, age, phone, email, positionId, null); // ID is auto-generated, positionName not needed for insert
            employeeDAO.addEmployee(newEmployee);
            response.sendRedirect(request.getContextPath() + "/employee/list?success=add");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid number format for age or position ID.");
            showNewForm(request, response);
        }
    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            String gender = request.getParameter("gender");
            int age = Integer.parseInt(request.getParameter("age"));
            String phone = request.getParameter("phone");
            String email = request.getParameter("email");
            int positionId = Integer.parseInt(request.getParameter("positionId"));
            
            if (name == null || name.trim().isEmpty() || gender == null || gender.trim().isEmpty() ||
                phone == null || phone.trim().isEmpty() || email == null || email.trim().isEmpty()) {
                request.setAttribute("errorMessage", "All fields are required.");
                // Need to fetch employee and position list again for the form
                EmployeeBean existingEmployee = employeeDAO.getEmployeeById(id);
                List<PositionBean> positionList = positionDAO.getAllPositions();
                request.setAttribute("employee", existingEmployee);
                request.setAttribute("positionList", positionList);
                request.getRequestDispatcher("/jsp/employee_edit.jsp").forward(request, response);
                return;
            }
             if (age <= 0) {
                 request.setAttribute("errorMessage", "Age must be a positive number.");
                 EmployeeBean existingEmployee = employeeDAO.getEmployeeById(id);
                 List<PositionBean> positionList = positionDAO.getAllPositions();
                 request.setAttribute("employee", existingEmployee);
                 request.setAttribute("positionList", positionList);
                 request.getRequestDispatcher("/jsp/employee_edit.jsp").forward(request, response);
                 return;
            }


            EmployeeBean employee = new EmployeeBean(id, name, gender, age, phone, email, positionId, null); // positionName not needed for update
            employeeDAO.updateEmployee(employee);
            response.sendRedirect(request.getContextPath() + "/employee/list?success=update");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid number format for ID, age, or position ID.");
            // Fetch data again for the edit form if ID was valid but other numbers were not
            try {
                int id = Integer.parseInt(request.getParameter("id")); // re-parse or get from a hidden field if necessary
                EmployeeBean existingEmployee = employeeDAO.getEmployeeById(id);
                List<PositionBean> positionList = positionDAO.getAllPositions();
                request.setAttribute("employee", existingEmployee);
                request.setAttribute("positionList", positionList);
            } catch (NumberFormatException nfe){
                 // if id itself was bad, this might fail too
            }
            request.getRequestDispatcher("/jsp/employee_edit.jsp").forward(request, response);
        }
    }

    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            employeeDAO.deleteEmployee(id);
            response.sendRedirect(request.getContextPath() + "/employee/list?success=delete");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid employee ID for deletion.");
            listEmployees(request, response); // Show list with error
        }
    }
}
