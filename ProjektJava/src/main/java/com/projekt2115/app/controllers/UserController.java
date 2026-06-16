package com.projekt2115.app.controllers;

import com.projekt2115.app.models.User;
import com.projekt2115.app.models.UserStatus;
import com.projekt2115.app.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("statuses", UserStatus.values());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (user.getBirthDate() != null) {
            java.time.Period wiek = java.time.Period.between(user.getBirthDate(), java.time.LocalDate.now());
            if (wiek.getYears() < 15) {
                bindingResult.rejectValue("birthDate", "error.user", "Musisz mieć ukończone co najmniej 15 lat, aby się zarejestrować!");
            }
        }


        if (bindingResult.hasErrors()) {
            model.addAttribute("statuses", UserStatus.values());
            return "register";
        }
        try {
            userService.saveUser(user);
            return "redirect:/users/success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("statuses", UserStatus.values());
            return "register";
        }
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "success";
    }

    @GetMapping("")
    public String listUsers(Model model) {
        model.addAttribute("usersList", userService.getAllUsers());
        return "usersList";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}