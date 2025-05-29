package com.example.controller;

import com.example.model.UserBean;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    private MockMvc mockMvc;

    private UserBean testUser;

    @BeforeEach
    void setUp() {
        // Setup view resolver for standalone setup to prevent warnings about no view resolver
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                               .setViewResolvers(viewResolver) // Optional: for more complete view resolution testing
                               .build();
        
        testUser = new UserBean();
        testUser.setId(1);
        testUser.setUsername("testuser");
        // No need for password in controller test as service handles that
    }

    @Test
    void testShowLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
               .andExpect(status().isOk())
               .andExpect(view().name("user_login"));
    }

    @Test
    void testHandleLogin_Success() throws Exception {
        when(userService.login("testuser", "password")).thenReturn(testUser);

        mockMvc.perform(post("/login")
                    .param("username", "testuser")
                    .param("password", "password"))
               .andExpect(status().is3xxRedirection()) // Expecting a redirect
               .andExpect(redirectedUrl("/employee/list"))
               .andExpect(request().sessionAttribute("loggedInUser", testUser))
               .andExpect(request().sessionAttribute("username", "testuser"));

        verify(userService).login("testuser", "password");
    }

    @Test
    void testHandleLogin_Failure() throws Exception {
        when(userService.login("testuser", "wrongpassword")).thenReturn(null);

        mockMvc.perform(post("/login")
                    .param("username", "testuser")
                    .param("password", "wrongpassword"))
               .andExpect(status().isOk()) // Stays on the login page
               .andExpect(view().name("user_login"))
               .andExpect(model().attributeExists("error"))
               // Corrected error message to match LoginController
               .andExpect(model().attribute("error", "Invalid username or password.")); 

        verify(userService).login("testuser", "wrongpassword");
    }
    
    @Test
    void testShowRootPage_UserLoggedIn() throws Exception {
        mockMvc.perform(get("/")
                    .sessionAttr("loggedInUser", testUser)) // Simulate user already logged in
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/employee/list"));
    }

    @Test
    void testShowRootPage_UserNotLoggedIn() throws Exception {
        mockMvc.perform(get("/")) // No loggedInUser in session
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/login"));
    }
}
