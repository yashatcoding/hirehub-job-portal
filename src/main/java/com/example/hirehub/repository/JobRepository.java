package com.example.hirehub.repository;

import com.example.hirehub.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
    // Abhi extra method ki zarurat nahi
}
