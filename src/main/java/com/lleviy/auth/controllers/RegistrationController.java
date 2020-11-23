package com.lleviy.auth.controllers;

import com.lleviy.auth.models.User;
import com.lleviy.auth.models.VerificationToken;
import com.lleviy.auth.services.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Calendar;

@Controller
public class RegistrationController {
    private IUserService service;

    public RegistrationController(IUserService service) {
        this.service = service;
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(@RequestParam("token") String token, RedirectAttributes redirAttrs) {
        VerificationToken verificationToken = service.getVerificationToken(token);
        if (verificationToken == null) {
            redirAttrs.addFlashAttribute("message", "Invalid token");
            redirAttrs.addFlashAttribute("alertClass", "alert-danger");
        }
        else {
            User user = verificationToken.getUser();
            Calendar cal = Calendar.getInstance();
            if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
                redirAttrs.addFlashAttribute("message", "Token expired");
                redirAttrs.addFlashAttribute("alertClass", "alert-danger");
            }
            else {
                user.setEnabled(true);
                service.saveRegisteredUser(user);
                redirAttrs.addFlashAttribute("message", "Email confirmed. You can sign in now.");
                redirAttrs.addFlashAttribute("alertClass", "alert-success");
            }
        }
        return "redirect:/login";
    }
}
