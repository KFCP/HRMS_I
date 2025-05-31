package com.example.controller;

import com.example.service.PositionService; // Import PositionService
import com.example.model.PositionBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/position")
public class PositionController {

    @Autowired
    private PositionService positionService; // Use PositionService

    @GetMapping({"/list", "/"})
    public String listPositions(Model model) {
        List<PositionBean> positionList = positionService.getAllPositions(); // Use positionService
        model.addAttribute("positionList", positionList);
        return "position_list";
    }

    @GetMapping("/add")
    public String showAddPositionForm(Model model) {
        model.addAttribute("position", new PositionBean());
        return "position_add";
    }

    @PostMapping("/add")
    public String addPosition(@ModelAttribute("position") PositionBean position,
                              RedirectAttributes redirectAttributes, Model model) { // Added model for error
        // Basic validation
        if (position.getPositionName() == null || position.getPositionName().trim().isEmpty() ||
            position.getLevel() <= 0) {
            model.addAttribute("errorMessage", "Position Name is required and Level must be positive.");
            model.addAttribute("position", position); // Send back the position with entered data
            return "position_add"; // Return to the add form
        }
        try {
            positionService.addPosition(position); // Use positionService
            redirectAttributes.addFlashAttribute("successMessage", "Position added successfully.");
            return "redirect:/position/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error adding position: " + e.getMessage());
            model.addAttribute("position", position);
            return "position_add";
        }
    }

    @GetMapping("/edit")
    public String showEditPositionForm(@RequestParam("id") int id, Model model, RedirectAttributes redirectAttributes) { // Added RedirectAttributes
        PositionBean position = positionService.getPositionById(id); // Use positionService
        if (position != null) {
            model.addAttribute("position", position);
            return "position_edit";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Position not found with ID: " + id);
            return "redirect:/position/list";
        }
    }

    @PostMapping("/update")
    public String updatePosition(@ModelAttribute("position") PositionBean position,
                                 RedirectAttributes redirectAttributes, Model model) { // Added model for error
        // Basic validation
        if (position.getPositionName() == null || position.getPositionName().trim().isEmpty() ||
            position.getLevel() <= 0) {
            model.addAttribute("errorMessage", "Position Name is required and Level must be positive.");
            model.addAttribute("position", position); // Send back the position with current data
            return "position_edit"; // Return to the edit form
        }
        try {
            positionService.updatePosition(position); // Use positionService
            redirectAttributes.addFlashAttribute("successMessage", "Position updated successfully.");
            return "redirect:/position/list";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating position: " + e.getMessage());
            model.addAttribute("position", position);
            return "position_edit";
        }
    }

    @GetMapping("/delete")
    public String deletePosition(@RequestParam("id") int id, RedirectAttributes redirectAttributes) {
        // Business logic for checking if position is in use could be in the service layer.
        // For now, direct delete.
        try {
            positionService.deletePosition(id); // Use positionService
            redirectAttributes.addFlashAttribute("successMessage", "Position deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting position: " + e.getMessage());
        }
        return "redirect:/position/list";
    }
}
