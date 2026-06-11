package com.projekt2115.app.controllers;


import com.projekt2115.app.models.Role;
import com.projekt2115.app.models.User;
import com.projekt2115.app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {

        model.addAttribute("user",new User());

        model.addAttribute("roles", Role.values());

        return "register";
    }

    @PostMapping("/register")
    public String registerUser (@ModelAttribute("user") User user, Model model) {
        try{
            userService.saveUser(user);
            return "redirect:/users/success";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("roles", Role.values());
            return "register";
        }
    }
    @GetMapping("/success")
    public String showSuccessPage() {
        return "success";
    }
    @GetMapping("")
    public String listUsers(Model model){
        model.addAttribute("usersList",userService.getAllUsers());
        return "usersList";
    }
    @GetMapping("/delete/{id}")
    public  String deleteUser(@PathVariable Long id){
        userService.deletUser(id);
        return "redirect:/users";
    }
}
