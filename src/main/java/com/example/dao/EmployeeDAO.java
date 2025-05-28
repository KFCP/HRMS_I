package com.example.dao;

import com.example.model.EmployeeBean;
import java.util.List;

public interface EmployeeDAO {
    void addEmployee(EmployeeBean employee);
    EmployeeBean getEmployeeById(int id);
    List<EmployeeBean> getAllEmployees();
    List<EmployeeBean> getEmployeesByName(String name);
    void updateEmployee(EmployeeBean employee);
    void deleteEmployee(int id);
}
