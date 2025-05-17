package com.serena.jobportal.controller;

import com.serena.jobportal.model.Application;
import com.serena.jobportal.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable String id) {
        return ResponseEntity.ok(applicationService.getApplicationById(id));
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<Application>> getApplicationsByCandidateId(@PathVariable String candidateId) {
        return ResponseEntity.ok(applicationService.getApplicationsByCandidateId(candidateId));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> getApplicationsByJobId(@PathVariable String jobId) {
        return ResponseEntity.ok(applicationService.getApplicationsByJobId(jobId));
    }

    @GetMapping("/count/job/{jobId}")
    public ResponseEntity<Map<String, Long>> countApplicationsByJobId(@PathVariable String jobId) {
        long count = applicationService.countApplicationsByJobId(jobId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @GetMapping("/count/candidate/{candidateId}")
    public ResponseEntity<Map<String, Long>> countApplicationsByCandidateId(@PathVariable String candidateId) {
        long count = applicationService.countApplicationsByCandidateId(candidateId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PostMapping
    public ResponseEntity<Application> createApplication(@Valid @RequestBody Application application) {
        Application createdApplication = applicationService.createApplication(application);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdApplication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable String id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/job/{jobId}")
    public ResponseEntity<Void> deleteApplicationsByJobId(@PathVariable String jobId) {
        applicationService.deleteApplicationsByJobId(jobId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/candidate/{candidateId}")
    public ResponseEntity<Map<String, Long>> deleteApplicationsByCandidateId(@PathVariable String candidateId) {
        long deleted = applicationService.deleteApplicationsByCandidateId(candidateId);
        return ResponseEntity.ok(Map.of("deleted", deleted));
    }
}