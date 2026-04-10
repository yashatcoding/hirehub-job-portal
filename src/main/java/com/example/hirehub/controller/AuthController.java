package com.example.hirehub.controller;

import com.example.hirehub.entity.User;
import com.example.hirehub.repository.UserRepository;
import com.example.hirehub.service.EmailService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    // ================= REGISTER PAGE =================
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    // ================= REGISTER PROCESS =================
    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("user") User user,
                                 BindingResult result,
                                 Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        // 🔥 FIX: trim + lowercase
        String email = user.getEmail().trim().toLowerCase();

        User existingUser = userRepository.findByEmail(email);

        if (existingUser != null) {
            model.addAttribute("error", "Email already registered!");
            return "register";
        }

        // save clean email
        user.setEmail(email);
        userRepository.save(user);

        // 🔥 EMAIL SEND
        emailService.sendEmail(
                email,
                "Welcome to HireHub",
                "Hello " + user.getName() + ", your registration was successful!"
        );

        model.addAttribute("success", "Registration successful!");

        return "register";
    }

    // ================= LOGIN PAGE =================
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // ================= LOGIN PROCESS =================
    @PostMapping("/login")
    public String handleLogin(@RequestParam String email,
                              @RequestParam String password,
                              Model model,
                              HttpSession session) {

        // trim email
        email = email.trim().toLowerCase();

        User user = userRepository.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            return "redirect:/user/dashboard/" + user.getId();
        }

        model.addAttribute("error", "Invalid credentials");
        return "login";
    }

    // ================= LOGOUT =================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // ================= FORGOT PASSWORD PAGE =================
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    // ================= FORGOT PASSWORD PROCESS =================
    @PostMapping("/forgot-password")
    public String handleForgotPassword(@RequestParam String email,
                                       Model model) {

        email = email.trim().toLowerCase();

        User user = userRepository.findByEmail(email);

        if (user == null) {
            model.addAttribute("error", "Email not found!");
            return "forgot-password";
        }

        emailService.sendEmail(
                email,
                "HireHub Password Recovery",
                "Hello " + user.getName() + ", your password is: " + user.getPassword()
        );

        model.addAttribute("success", "Password sent to your email!");

        return "forgot-password";
    }
}