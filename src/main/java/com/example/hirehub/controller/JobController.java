package com.example.hirehub.controller;

import com.example.hirehub.entity.Job;

import com.example.hirehub.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    // 🔹 Jobs List Page
    @GetMapping("/jobs")
    public String jobsPage(Model model) {
        List<Job> jobs = jobRepository.findAll();
        model.addAttribute("jobs", jobs);
        return "jobs";
    }

    // 🔹 Job Details Page
    @GetMapping("/jobs/{id}")
    public String jobDetailsPage(@PathVariable Long id, Model model) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        model.addAttribute("job", job);
        return "job-details";
    }
    
    @GetMapping("/apply/{id}")
    public String applyJob(@PathVariable Long id, Model model) {

        Job job = jobRepository.findById(id).orElse(null);

        model.addAttribute("job", job);

        return "apply-job";
    }
    @PostMapping("/apply")
    public String submitApplication(@RequestParam Long jobId, Model model) {

        System.out.println("Job applied for ID: " + jobId);

        model.addAttribute("message", "Application submitted successfully!");

        return "application-success";
    }
}
