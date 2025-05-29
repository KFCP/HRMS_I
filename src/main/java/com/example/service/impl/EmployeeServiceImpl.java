package com.example.service.impl;

import com.example.dao.EmployeeDAO;
import com.example.model.EmployeeBean;
import com.example.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeDAO employeeDAO;

    @Override
    public List<EmployeeBean> getAllEmployees() {
        return employeeDAO.getAllEmployees();
    }

    @Override
    public List<EmployeeBean> searchEmployees(String name) {
        // Add wildcards for the search pattern
        String namePattern = "%" + name + "%";
        return employeeDAO.searchEmployeesByName(namePattern);
    }

    @Override
    public EmployeeBean getEmployeeById(int employeeId) {
        return employeeDAO.getEmployeeById(employeeId);
    }

    @Override
    @Transactional
    public void addEmployee(EmployeeBean employeeBean) {
        // Business logic, e.g., validation, can be added here
        employeeDAO.addEmployee(employeeBean);
    }

    @Override
    @Transactional
    public void updateEmployee(EmployeeBean employeeBean) {
        // Business logic, e.g., validation, can be added here
        employeeDAO.updateEmployee(employeeBean);
    }

    @Override
    @Transactional
    public void deleteEmployee(int employeeId) {
        employeeDAO.deleteEmployee(employeeId);
    }
}
