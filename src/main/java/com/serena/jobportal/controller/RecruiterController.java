package com.serena.jobportal.controller;

import com.serena.jobportal.model.Recruiter;
import com.serena.jobportal.service.RecruiterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiters")
public class RecruiterController {

    @Autowired
    private RecruiterService recruiterService;

    @GetMapping
    public ResponseEntity<List<Recruiter>> getAllRecruiters() {
        return ResponseEntity.ok(recruiterService.getAllRecruiters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recruiter> getRecruiterById(@PathVariable String id) {
        return ResponseEntity.ok(recruiterService.getRecruiterById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Recruiter> getRecruiterByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(recruiterService.getRecruiterByUserId(userId));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Recruiter>> getRecruitersByCompanyId(@PathVariable String companyId) {
        return ResponseEntity.ok(recruiterService.getRecruitersByCompanyId(companyId));
    }

    @PostMapping
    public ResponseEntity<Recruiter> createRecruiter(@Valid @RequestBody Recruiter recruiter) {
        Recruiter createdRecruiter = recruiterService.createRecruiter(recruiter);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecruiter);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Recruiter> updateRecruiter(@PathVariable String id, @Valid @RequestBody Recruiter recruiterDetails) {
        return ResponseEntity.ok(recruiterService.updateRecruiter(id, recruiterDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruiter(@PathVariable String id) {
        recruiterService.deleteRecruiter(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteRecruiterByUserId(@PathVariable String userId) {
        recruiterService.deleteRecruiterByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}