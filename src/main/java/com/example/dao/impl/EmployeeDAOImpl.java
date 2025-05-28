package com.example.dao.impl;

import com.example.dao.EmployeeDAO;
import com.example.model.EmployeeBean;
import com.example.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO {

    @Override
    public void addEmployee(EmployeeBean employee) {
        String sql = "INSERT INTO employees (name, gender, age, phone, email, position_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getGender());
            pstmt.setInt(3, employee.getAge());
            pstmt.setString(4, employee.getPhone());
            pstmt.setString(5, employee.getEmail());
            pstmt.setInt(6, employee.getPositionId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public EmployeeBean getEmployeeById(int id) {
        String sql = "SELECT e.*, p.position_name FROM employees e JOIN positions p ON e.position_id = p.id WHERE e.id = ?";
        EmployeeBean employee = null;
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    employee = new EmployeeBean();
                    employee.setId(rs.getInt("id"));
                    employee.setName(rs.getString("name"));
                    employee.setGender(rs.getString("gender"));
                    employee.setAge(rs.getInt("age"));
                    employee.setPhone(rs.getString("phone"));
                    employee.setEmail(rs.getString("email"));
                    employee.setPositionId(rs.getInt("position_id"));
                    employee.setPositionName(rs.getString("position_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employee;
    }

    @Override
    public List<EmployeeBean> getAllEmployees() {
        String sql = "SELECT e.*, p.position_name FROM employees e JOIN positions p ON e.position_id = p.id ORDER BY e.id";
        List<EmployeeBean> employees = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                EmployeeBean employee = new EmployeeBean();
                employee.setId(rs.getInt("id"));
                employee.setName(rs.getString("name"));
                employee.setGender(rs.getString("gender"));
                employee.setAge(rs.getInt("age"));
                employee.setPhone(rs.getString("phone"));
                employee.setEmail(rs.getString("email"));
                employee.setPositionId(rs.getInt("position_id"));
                employee.setPositionName(rs.getString("position_name"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public List<EmployeeBean> getEmployeesByName(String name) {
        String sql = "SELECT e.*, p.position_name FROM employees e JOIN positions p ON e.position_id = p.id WHERE e.name LIKE ? ORDER BY e.id";
        List<EmployeeBean> employees = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + name + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    EmployeeBean employee = new EmployeeBean();
                    employee.setId(rs.getInt("id"));
                    employee.setName(rs.getString("name"));
                    employee.setGender(rs.getString("gender"));
                    employee.setAge(rs.getInt("age"));
                    employee.setPhone(rs.getString("phone"));
                    employee.setEmail(rs.getString("email"));
                    employee.setPositionId(rs.getInt("position_id"));
                    employee.setPositionName(rs.getString("position_name"));
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    @Override
    public void updateEmployee(EmployeeBean employee) {
        String sql = "UPDATE employees SET name = ?, gender = ?, age = ?, phone = ?, email = ?, position_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getGender());
            pstmt.setInt(3, employee.getAge());
            pstmt.setString(4, employee.getPhone());
            pstmt.setString(5, employee.getEmail());
            pstmt.setInt(6, employee.getPositionId());
            pstmt.setInt(7, employee.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
