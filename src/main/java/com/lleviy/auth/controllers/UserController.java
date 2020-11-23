package com.lleviy.auth.controllers;

import com.lleviy.auth.dto.EditUserDto;
import com.lleviy.auth.dto.UserDto;
import com.lleviy.auth.event.OnRegistrationCompleteEvent;
import com.lleviy.auth.models.User;
import com.lleviy.auth.services.IUserService;
import exceptions.UserAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@Controller
public class UserController {

    private IUserService userService;
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    public UserController(IUserService userService, ApplicationEventPublisher eventPublisher){
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    @GetMapping("/signup")
    public String showRegistrationForm(Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "signup";
    }

    @PostMapping("/signup")
    public ModelAndView signUp(@ModelAttribute("user") @Valid UserDto user, HttpServletRequest request, Errors errors) {
        ModelAndView mav = new ModelAndView("signup", "user", user);
        if (errors.hasErrors()) {
            return mav;
        }
        try {
            User registered = userService.registerNewUserAccount(user);

            String appUrl = request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, appUrl));
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

    @GetMapping("profile/edit")
    public String showUpdateForm(Model model) {
        EditUserDto editUserDto = userService.getCurrentEditUserDto();
        model.addAttribute("user", editUserDto);
        return "updateUser";
    }

    @PostMapping("profile/edit")
    public String updateUser(@ModelAttribute("user") @Valid EditUserDto editUserDto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "updateUser";
        }
        userService.editUser(editUserDto);
        model.addAttribute("message", "Information successfully updated.");
        model.addAttribute("alertClass", "alert-success");
        return "updateUser";
    }
}
