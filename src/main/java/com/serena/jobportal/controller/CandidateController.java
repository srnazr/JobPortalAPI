package com.serena.jobportal.controller;

import com.serena.jobportal.model.Candidate;
import com.serena.jobportal.service.CandidateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable String id) {
        return ResponseEntity.ok(candidateService.getCandidateById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Candidate> getCandidateByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(candidateService.getCandidateByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<Candidate> createCandidate(@Valid @RequestBody Candidate candidate) {
        Candidate createdCandidate = candidateService.createCandidate(candidate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCandidate);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidate> updateCandidate(@PathVariable String id, @Valid @RequestBody Candidate candidateDetails) {
        return ResponseEntity.ok(candidateService.updateCandidate(id, candidateDetails));
    }

    @PostMapping("/{id}/skills")
    public ResponseEntity<Candidate> addSkill(@PathVariable String id, @Valid @RequestBody Candidate.Skill skill) {
        return ResponseEntity.ok(candidateService.addSkill(id, skill));
    }

    @PostMapping("/{id}/educations")
    public ResponseEntity<Candidate> addEducation(@PathVariable String id, @Valid @RequestBody Candidate.Education education) {
        return ResponseEntity.ok(candidateService.addEducation(id, education));
    }

    @PostMapping("/{id}/experiences")
    public ResponseEntity<Candidate> addExperience(@PathVariable String id, @Valid @RequestBody Candidate.Experience experience) {
        return ResponseEntity.ok(candidateService.addExperience(id, experience));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable String id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }
}