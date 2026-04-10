package com.example.hirehub.controller;

import com.example.hirehub.entity.Feedback;
import com.example.hirehub.repository.FeedbackRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    // form page
    @GetMapping("/feedback")
    public String feedbackPage(Model model) {
        model.addAttribute("feedback", new Feedback());
        return "feedback";
    }

    // save feedback
    @PostMapping("/feedback")
    public String saveFeedback(@ModelAttribute Feedback feedback, Model model) {

        feedbackRepository.save(feedback);

        model.addAttribute("success", "Feedback submitted successfully!");

        return "feedback";
    }
}