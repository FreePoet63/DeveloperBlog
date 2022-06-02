package com.razlivinskiyproject.developerBlog.controllers;

import com.razlivinskiyproject.developerBlog.models.User;
import com.razlivinskiyproject.developerBlog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid User userForm,
                          BindingResult bindingResult,
                          Model model) {
        if(!userForm.getPassword().equals(userForm.getConfirmPassword())) {
            model.addAttribute("passwordError", "passwords are different");
            return "registration";
        }
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!userService.createUser(userForm)) {
            model.addAttribute("userNameError", "Пользователь с таким именем уже существует");
            return "registration";
        }
        return "redirect:/login";
    }
}
