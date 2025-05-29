package com.example.service;

import com.example.model.EmployeeBean;
import java.util.List;

public interface EmployeeService {
    List<EmployeeBean> getAllEmployees();
    List<EmployeeBean> searchEmployees(String name);
    EmployeeBean getEmployeeById(int employeeId);
    void addEmployee(EmployeeBean employeeBean);
    void updateEmployee(EmployeeBean employeeBean);
    void deleteEmployee(int employeeId);
}
