package com.example.hirehub.controller;

import com.example.hirehub.entity.User;
import com.example.hirehub.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // ================= DASHBOARD =================
    @GetMapping("/dashboard/{id}")
    public String dashboard(@PathVariable Long id,
                             Model model,
                             HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/login"; // ❌ access block
        }

        User user = userRepository.findById(id).orElse(null);
        model.addAttribute("user", user);

        return "user/dashboard";
    }

    // ================= EDIT PROFILE =================
    @GetMapping("/edit/{id}")
    public String editProfile(@PathVariable Long id, Model model) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);

        return "user/edit-profile";
    }

    // ================= UPDATE PROFILE =================
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User user) {

        userRepository.save(user);

        return "redirect:/user/dashboard/" + user.getId();
    }

    // ================= CHANGE PASSWORD PAGE =================
    @GetMapping("/change-password/{id}")
    public String changePasswordPage(@PathVariable Long id, Model model) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);

        return "user/change-password";
    }

    // ================= UPDATE PASSWORD =================
    @PostMapping("/change-password")
    public String changePassword(@RequestParam Long id,
                                 @RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 Model model) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // ❌ Old password wrong
        if (!user.getPassword().equals(oldPassword)) {
            model.addAttribute("user", user);
            model.addAttribute("error", "Old password is incorrect!");
            return "user/change-password";
        }

        // ✅ Update password
        user.setPassword(newPassword);
        userRepository.save(user);

        model.addAttribute("user", user);
        model.addAttribute("success", "Password updated successfully!");

        return "user/change-password";
    }

}