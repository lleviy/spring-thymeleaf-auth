package com.lleviy.auth.controllers;

import com.lleviy.auth.dto.UserDTO;
import com.lleviy.auth.models.User;
import com.lleviy.auth.services.UserService;
import exceptions.UserAlreadyExistException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String showRegistrationForm(Model model) {
        UserDTO userDto = new UserDTO();
        model.addAttribute("user", userDto);
        return "signup";
    }

    @PostMapping("/signup")
    public ModelAndView signUp(@ModelAttribute("user") @Valid UserDTO user, HttpServletRequest request, Errors errors) {
        ModelAndView mav = new ModelAndView("signup", "user", user);
        if (errors.hasErrors()) {
            return mav;
        }
        try {
            User registered = userService.registerNewUserAccount(user);
        }
        catch (UserAlreadyExistException uaeEx) {
            mav.addObject("message", uaeEx.getMessage());
            mav.addObject("alertClass", "alert-danger");
            return mav;
        }
        mav.addObject("message", "A letter has been sent to your email. " +
                "To confirm your email and complete registration, please follow the link from the letter.");
        mav.addObject("alertClass", "alert-success");
        return mav;
    }
}
