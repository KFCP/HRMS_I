package com.example.service.impl;

import com.example.dao.UserDAO;
import com.example.model.UserBean;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Override
    public UserBean login(String username, String password) {
        if (userDAO.isValidUser(username, password)) {
            return userDAO.getUserByUsername(username);
        }
        return null;
    }

    @Override
    @Transactional
    public void registerUser(UserBean userBean) throws Exception {
        if (userDAO.getUserByUsername(userBean.getUsername()) != null) {
            throw new Exception("User already exists with username: " + userBean.getUsername());
        }
        // Password hashing should ideally be handled here or in DAO before saving
        // For now, assuming UserDAO handles it or it's plain text as per current DAO structure
        userDAO.addUser(userBean);
    }

    @Override
    @Transactional
    public void updateUser(UserBean userBean) throws Exception {
        // Add any specific validation or business logic before updating
        // For example, check if the user exists by ID
        UserBean existingUser = userDAO.getUserById(userBean.getId());
        if (existingUser == null) {
            throw new Exception("User not found with ID: " + userBean.getId());
        }
        // Handle password update logic if password field is part of UserBean and meant to be updated
        // If password in userBean is null or empty, existingUser's password should be retained by DAO or here
        userDAO.updateUser(userBean);
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        userDAO.deleteUser(userId);
    }

    @Override
    public UserBean getUserById(int userId) {
        return userDAO.getUserById(userId);
    }

    @Override
    public List<UserBean> getAllUsers() {
        return userDAO.getAllUsers();
    }

    @Override
    public UserBean getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }
}
