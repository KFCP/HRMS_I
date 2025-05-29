package com.example.service.impl;

import com.example.dao.UserDAO;
import com.example.model.UserBean;
// import com.example.service.UserService; // Not directly used in this test class, UserServiceImpl is used
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private UserServiceImpl userService; // Use UserServiceImpl for testing its implementation

    private UserBean testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserBean();
        testUser.setId(1);
        testUser.setUsername("testuser");
        testUser.setPassword("password"); // In real tests, use hashed passwords if service handles hashing
    }

    @Test
    void testLogin_Success() {
        when(userDAO.isValidUser("testuser", "password")).thenReturn(true);
        when(userDAO.getUserByUsername("testuser")).thenReturn(testUser);

        UserBean loggedInUser = userService.login("testuser", "password");

        assertNotNull(loggedInUser);
        assertEquals("testuser", loggedInUser.getUsername());
        verify(userDAO).isValidUser("testuser", "password");
        verify(userDAO).getUserByUsername("testuser");
    }

    @Test
    void testLogin_Failure_InvalidCredentials() {
        when(userDAO.isValidUser("testuser", "wrongpassword")).thenReturn(false);
        // No need to mock getUserByUsername if isValidUser is false, as it shouldn't be called by this logic path in service.

        UserBean loggedInUser = userService.login("testuser", "wrongpassword");

        assertNull(loggedInUser);
        verify(userDAO).isValidUser("testuser", "wrongpassword");
        verify(userDAO, never()).getUserByUsername(anyString());
    }

    @Test
    void testRegisterUser_Success_NewUser() throws Exception {
        when(userDAO.getUserByUsername("newuser")).thenReturn(null);
        // doNothing().when(userDAO).addUser(any(UserBean.class)); // Not strictly needed if addUser has void return

        UserBean newUser = new UserBean();
        newUser.setUsername("newuser");
        newUser.setPassword("newpassword");
        
        userService.registerUser(newUser);

        verify(userDAO).getUserByUsername("newuser");
        verify(userDAO).addUser(newUser); // Check if addUser was called
    }

    @Test
    void testRegisterUser_Failure_UserExists() {
        when(userDAO.getUserByUsername("testuser")).thenReturn(testUser);
         UserBean existingUser = new UserBean();
         existingUser.setUsername("testuser");
         existingUser.setPassword("password");

        Exception exception = assertThrows(Exception.class, () -> {
            userService.registerUser(existingUser);
        });
        
        assertEquals("User already exists with username: testuser", exception.getMessage());
        verify(userDAO).getUserByUsername("testuser");
        verify(userDAO, never()).addUser(any(UserBean.class));
    }
}
