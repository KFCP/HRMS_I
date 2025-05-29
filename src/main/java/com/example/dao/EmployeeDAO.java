package com.example.dao;

import com.example.model.EmployeeBean;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface EmployeeDAO {
    void addEmployee(EmployeeBean employee);
    EmployeeBean getEmployeeById(@Param("id") int id);
    List<EmployeeBean> getAllEmployees();
    List<EmployeeBean> searchEmployeesByName(@Param("namePattern") String namePattern); // Renamed and added @Param
    void updateEmployee(EmployeeBean employee);
    void deleteEmployee(@Param("id") int id);
}
