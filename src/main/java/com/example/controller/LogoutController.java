package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession; // Already Jakarta

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        // Redirect to the login page, possibly with a query parameter to indicate logout
        return "redirect:/login?logout";
    }
}
