package com.example.controller;

import com.example.model.UserBean;
import com.example.service.UserService; // Import UserService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping; // Not used
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UserService userService; // Use UserService

    @GetMapping("/")
    public String rootPath(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            return "redirect:/employee/list"; // Or a general dashboard page
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        // This will resolve to /WEB-INF/jsp/user_login.jsp based on view resolver
        return "user_login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
                               @RequestParam("password") String password,
                               HttpSession session,
                               Model model) {

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            model.addAttribute("error", "Username and password are required.");
            return "user_login";
        }

        UserBean user = userService.login(username, password); // Use userService.login()

        if (user != null) {
            session.setAttribute("loggedInUser", user);
            session.setAttribute("username", user.getUsername()); // For compatibility if needed
            return "redirect:/employee/list"; // Redirect to a dashboard or main page
        } else {
            model.addAttribute("error", "Invalid username or password.");
            return "user_login";
        }
    }
}
