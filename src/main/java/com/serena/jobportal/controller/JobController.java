package com.serena.jobportal.controller;

import com.serena.jobportal.model.Job;
import com.serena.jobportal.service.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable String id) {
        return ResponseEntity.ok(jobService.getJobById(id));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<Job>> getJobsByTitle(@PathVariable String title) {
        return ResponseEntity.ok(jobService.getJobsByTitle(title));
    }

    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<Job>> getJobsByRecruiterId(@PathVariable String recruiterId) {
        return ResponseEntity.ok(jobService.getJobsByRecruiterId(recruiterId));
    }

    @GetMapping("/employment-type/{type}")
    public ResponseEntity<List<Job>> getJobsByEmploymentType(@PathVariable String type) {
        return ResponseEntity.ok(jobService.getJobsByEmploymentType(type));
    }

    @GetMapping("/search/title")
    public ResponseEntity<List<Job>> searchJobsByTitle(@RequestParam String keyword) {
        return ResponseEntity.ok(jobService.searchJobsByTitle(keyword));
    }

    @GetMapping("/search/description")
    public ResponseEntity<List<Job>> searchJobsByDescription(@RequestParam String keyword) {
        return ResponseEntity.ok(jobService.searchJobsByDescription(keyword));
    }

    @PostMapping
    public ResponseEntity<Job> createJob(@Valid @RequestBody Job job) {
        Job createdJob = jobService.createJob(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable String id, @Valid @RequestBody Job jobDetails) {
        return ResponseEntity.ok(jobService.updateJob(id, jobDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable String id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}