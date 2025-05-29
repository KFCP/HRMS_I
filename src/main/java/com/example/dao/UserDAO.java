package com.example.dao;

import com.example.model.UserBean;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface UserDAO {
    void addUser(UserBean user);
    UserBean getUserByUsername(@Param("username") String username); // Add @Param for clarity, though not strictly needed for single param
    boolean isValidUser(@Param("username") String username, @Param("password") String password);
    List<UserBean> getAllUsers();
    void updateUser(UserBean user);
    void deleteUser(@Param("id") int id); // Add @Param for clarity
    UserBean getUserById(@Param("id") int id); // Add new method
}
