package com.example.controller;

// Import services
import com.example.service.EmployeeService;
import com.example.service.PositionService;
import com.example.model.EmployeeBean;
import com.example.model.PositionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService; // Use EmployeeService

    @Autowired
    private PositionService positionService; // Use PositionService

    @GetMapping({"/list", "/"})
    public String listEmployees(@RequestParam(value = "searchName", required = false) String searchName, Model model) {
        List<EmployeeBean> employeeList;
        if (searchName != null && !searchName.trim().isEmpty()) {
            employeeList = employeeService.searchEmployees(searchName); // Use employeeService
            model.addAttribute("searchName", searchName);
        } else {
            employeeList = employeeService.getAllEmployees(); // Use employeeService
        }
        model.addAttribute("employeeList", employeeList);
        return "employee_list"; 
    }

    @GetMapping("/add")
    public String showAddEmployeeForm(Model model) {
        List<PositionBean> positionList = positionService.getAllPositions(); // Use positionService
        model.addAttribute("employee", new EmployeeBean()); 
        model.addAttribute("positionList", positionList);
        return "employee_add"; 
    }

    @PostMapping("/add")
    public String addEmployee(@ModelAttribute("employee") EmployeeBean employee,
                              RedirectAttributes redirectAttributes, Model model) { // Added Model for error case
        // Basic validation (can be enhanced with @Valid and BindingResult)
        if (employee.getName() == null || employee.getName().trim().isEmpty() ||
            employee.getGender() == null || employee.getGender().trim().isEmpty() ||
            employee.getPhone() == null || employee.getPhone().trim().isEmpty() ||
            employee.getEmail() == null || employee.getEmail().trim().isEmpty() ||
            employee.getAge() <= 0 || employee.getPositionId() == 0) {
            
            // If validation fails, return to the form with an error message and existing data
            model.addAttribute("errorMessage", "All fields are required and age must be positive.");
            // Repopulate position list for the form
            List<PositionBean> positionList = positionService.getAllPositions();
            model.addAttribute("positionList", positionList);
            model.addAttribute("employee", employee); // Send back the employee with entered data
            return "employee_add"; // Return to the add form
        }
        try {
            employeeService.addEmployee(employee); // Use employeeService
            redirectAttributes.addFlashAttribute("successMessage", "Employee added successfully.");
            return "redirect:/employee/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error adding employee: " + e.getMessage());
            List<PositionBean> positionList = positionService.getAllPositions();
            model.addAttribute("positionList", positionList);
            model.addAttribute("employee", employee);
            return "employee_add";
        }
    }

    @GetMapping("/edit")
    public String showEditEmployeeForm(@RequestParam("id") int id, Model model, RedirectAttributes redirectAttributes) {
        EmployeeBean employee = employeeService.getEmployeeById(id); // Use employeeService
        List<PositionBean> positionList = positionService.getAllPositions(); // Use positionService
        if (employee != null) {
            model.addAttribute("employee", employee);
            model.addAttribute("positionList", positionList);
            return "employee_edit"; 
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Employee not found with ID: " + id);
            return "redirect:/employee/list";
        }
    }

    @PostMapping("/update") 
    public String updateEmployee(@ModelAttribute("employee") EmployeeBean employee,
                                 RedirectAttributes redirectAttributes, Model model) { // Added Model for error case
        // Basic validation
        if (employee.getName() == null || employee.getName().trim().isEmpty() ||
            employee.getGender() == null || employee.getGender().trim().isEmpty() ||
            employee.getPhone() == null || employee.getPhone().trim().isEmpty() ||
            employee.getEmail() == null || employee.getEmail().trim().isEmpty() ||
            employee.getAge() <= 0 || employee.getPositionId() == 0) {

            model.addAttribute("errorMessage", "All fields are required and age must be positive.");
            List<PositionBean> positionList = positionService.getAllPositions();
            model.addAttribute("positionList", positionList);
            model.addAttribute("employee", employee); // Send back the employee with current data
            return "employee_edit"; // Return to the edit form
        }
        try {
            employeeService.updateEmployee(employee); // Use employeeService
            redirectAttributes.addFlashAttribute("successMessage", "Employee updated successfully.");
            return "redirect:/employee/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating employee: " + e.getMessage());
            List<PositionBean> positionList = positionService.getAllPositions();
            model.addAttribute("positionList", positionList);
            model.addAttribute("employee", employee);
            return "employee_edit";
        }
    }

    @GetMapping("/delete")
    public String deleteEmployee(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        try {
            employeeService.deleteEmployee(id); // Use employeeService
            redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting employee: " + e.getMessage());
        }
        return "redirect:/employee/list";
    }
}
