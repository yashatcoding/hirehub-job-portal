package com.example.hirehub.controller;

import com.example.hirehub.entity.User;
import com.example.hirehub.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // ================= ADMIN LOGIN =================
    @GetMapping("/login")
    public String adminLoginPage() {
        return "admin/admin-login";
    }

    @PostMapping("/login")
    public String handleAdminLogin(@RequestParam String email,
                                   @RequestParam String password,
                                   Model model) {

        if(email == null || email.trim().isEmpty() ||
           password == null || password.trim().isEmpty()) {

            model.addAttribute("error", "Email and Password are required");
            return "admin/admin-login";
        }

        if(email.equals("admin@gmail.com") && password.equals("1234")) {
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("error", "Invalid Admin Credentials");
        return "admin/admin-login";
    }

    // ================= DASHBOARD =================
    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {

        long totalUsers = userRepository.count();

        model.addAttribute("totalUsers", totalUsers);

        return "admin/dashboard";
    }

    // ================= USERS LIST =================
    @GetMapping("/users")
    public String viewUsers(Model model) {

        model.addAttribute("users", userRepository.findAll());

        return "admin/user-list";
    }

    // ================= DELETE USER =================
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id,
                             RedirectAttributes ra) {

        userRepository.deleteById(id);

        ra.addFlashAttribute("success", "User deleted successfully!");

        return "redirect:/admin/users";
    }

    // ================= UPDATE USER =================
    @PostMapping("/update")
    public String updateUser(@RequestParam Long id,
                             @RequestParam String name,
                             @RequestParam String email,
                             RedirectAttributes ra) {

        User user = userRepository.findById(id).orElse(null);

        if(user != null){
            user.setName(name);
            user.setEmail(email);
            userRepository.save(user);
        }

        ra.addFlashAttribute("success", "User updated successfully!");

        return "redirect:/admin/users";
    }

}