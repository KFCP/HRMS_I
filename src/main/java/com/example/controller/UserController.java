package com.example.controller;

import com.example.model.UserBean;
import com.example.service.UserService; // Import UserService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService; // Use UserService

    @GetMapping({"/list", "/"})
    public String listUsers(Model model) {
        List<UserBean> userList = userService.getAllUsers(); // Use userService
        model.addAttribute("userList", userList);
        return "user_list";
    }

    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new UserBean());
        return "user_add";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("user") UserBean user,
                          @RequestParam("confirmPassword") String confirmPassword,
                          RedirectAttributes redirectAttributes, Model model) {

        if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
            user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            model.addAttribute("errorMessage", "Username and password are required.");
            model.addAttribute("user", user);
            return "user_add";
        }

        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Passwords do not match.");
            model.addAttribute("user", user);
            return "user_add";
        }

        try {
            userService.registerUser(user); // Use userService
            redirectAttributes.addFlashAttribute("successMessage", "User added successfully.");
            return "redirect:/user/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage()); // Display error from service
            model.addAttribute("user", user);
            return "user_add";
        }
    }

    @GetMapping("/edit")
    public String showEditUserForm(@RequestParam("id") int id, Model model, RedirectAttributes redirectAttributes) {
        UserBean user = userService.getUserById(id); // Use userService
        if (user != null) {
            // Do not add password to the model for editing display
            user.setPassword(null); // Clear password before sending to form
            model.addAttribute("user", user);
            return "user_edit";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found with ID: " + id);
            return "redirect:/user/list";
        }
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") UserBean user, // User object from form
                             @RequestParam(value="newPassword", required=false) String newPassword,
                             @RequestParam(value="confirmNewPassword", required=false) String confirmNewPassword,
                             RedirectAttributes redirectAttributes, Model model) {

        UserBean userToUpdate = userService.getUserById(user.getId());
        if (userToUpdate == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "User not found with ID: " + user.getId());
            return "redirect:/user/list";
        }

        // Update username if it's being changed
        if (user.getUsername() != null && !user.getUsername().trim().isEmpty() && !user.getUsername().equals(userToUpdate.getUsername())) {
             UserBean existingUserWithNewName = userService.getUserByUsername(user.getUsername());
             if (existingUserWithNewName != null && existingUserWithNewName.getId() != userToUpdate.getId()) {
                 model.addAttribute("errorMessage", "Username '" + user.getUsername() + "' already exists. Please choose a different one.");
                 user.setPassword(null); // Clear password field for security
                 model.addAttribute("user", user); // Send back user with the attempted new username
                 return "user_edit";
             }
            userToUpdate.setUsername(user.getUsername());
        }

        // Update password only if a new password is provided
        if (newPassword != null && !newPassword.isEmpty()) {
            if (!newPassword.equals(confirmNewPassword)) {
                model.addAttribute("errorMessage", "New passwords do not match.");
                user.setPassword(null); // Clear password field
                model.addAttribute("user", user); // Send back original user data from form
                return "user_edit";
            }
            userToUpdate.setPassword(newPassword); // Set new password for update
        } else {
            // If newPassword is empty, retain the old password.
            // UserDAO's updateUser should be designed to only update password if it's not null/empty in the bean.
            // Or, the service/DAO should explicitly fetch the user and only set fields that are meant to change.
            // For this setup, userService.updateUser will receive a UserBean. If its password field is null,
            // the mapper should ideally not update it. Assuming UserMapper.xml for updateUser handles this.
            // If UserMapper.xml *always* updates the password, then we must explicitly set userToUpdate.setPassword(null)
            // or set it to the *current* password if newPassword is not provided.
            // For simplicity here, if newPassword is not provided, the password in userToUpdate is not changed.
            // The UserServiceImpl's updateUser method will pass userToUpdate (with either old or new password) to DAO.
            // The UserMapper.xml for updateUser *will* update the password field.
            // So, if no new password, we should set the password from the form to null in userToUpdate,
            // or ensure the service.updateUser and dao.updateUser ignore null passwords.
            // Let's assume the mapper's updateUser will update password if it's set.
            // So if newPassword is empty, we don't set userToUpdate.password.
            // The current UserServiceImpl.updateUser takes the bean and passes it to DAO.
            // The UserMapper.xml's updateUser updates username AND password.
            // This means if password is not changed, we must pass the *original* hashed password.
            // The form for edit doesn't have the original hashed password.
            // This is a common complexity.
            // Solution: Modify updateUser in service/DAO to selectively update password.
            // For now, let's assume the service.updateUser is smart or DAO is.
            // A better approach is to have a specific DTO for password change or separate service method.
            // Given current UserMapper.xml updates password, if newPassword is empty, we should NOT set it on userToUpdate.
            // If newPassword is provided, it's set on userToUpdate. If not, userToUpdate.password remains (original from DB).
            // This is fine if userToUpdate.password is the hashed one.
            // The UserServiceImpl.updateUser should handle this logic.
            // The provided UserServiceImpl does: userDAO.updateUser(userBean).
            // The UserMapper for UserDAO updates password if it's in the bean.
            // So, if newPassword is empty, we should make sure userToUpdate.password is the current one (already is).
            // If newPassword is set, userToUpdate.password is set to newPassword.
        }

        try {
            userService.updateUser(userToUpdate);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully.");
            return "redirect:/user/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating user: " + e.getMessage());
            user.setPassword(null); // Clear password field
            model.addAttribute("user", user); // Send back user data from form
            return "user_edit";
        }
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        // Logic for preventing deletion of self or admin could be in service layer
        try {
            userService.deleteUser(id); // Use userService
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/user/list";
    }
}
