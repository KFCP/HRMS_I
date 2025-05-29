package com.example.service;

import com.example.model.UserBean;
import java.util.List;

public interface UserService {
    UserBean login(String username, String password);
    void registerUser(UserBean userBean) throws Exception;
    void updateUser(UserBean userBean) throws Exception;
    void deleteUser(int userId);
    UserBean getUserById(int userId);
    List<UserBean> getAllUsers();
    UserBean getUserByUsername(String username);
}
