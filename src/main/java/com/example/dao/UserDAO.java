package com.example.dao;

import com.example.model.UserBean;
import java.util.List;

public interface UserDAO {
    void addUser(UserBean user);
    UserBean getUserByUsername(String username);
    boolean isValidUser(String username, String password);
    List<UserBean> getAllUsers();
    void updateUser(UserBean user);
    void deleteUser(int id);
}
