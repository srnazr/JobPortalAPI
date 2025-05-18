package com.serena.jobportal.controller;

import com.serena.jobportal.dto.request.CandidateRequest;
import com.serena.jobportal.dto.response.CandidateResponse;
import com.serena.jobportal.model.Candidate;
import com.serena.jobportal.model.User;
import com.serena.jobportal.security.AuthenticationFacade;
import com.serena.jobportal.service.ApplicationService;
import com.serena.jobportal.service.CandidateService;
import com.serena.jobportal.service.UserService;
import com.serena.jobportal.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;
    private final UserService userService;
    private final ApplicationService applicationService;
    private final AuthenticationFacade authenticationFacade;

    @PostMapping
    public ResponseEntity<?> createCandidate(@Valid @RequestBody CandidateRequest candidateRequest) {
        String email = authenticationFacade.getCurrentUsername();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Candidate candidate = Candidate.builder()
                .resumeSummary(candidateRequest.getResumeSummary())
                .phoneNumber(candidateRequest.getPhoneNumber())
                .linkedInProfile(candidateRequest.getLinkedInProfile())
                .githubProfile(candidateRequest.getGithubProfile())
                .portfolioUrl(candidateRequest.getPortfolioUrl())
                .resumeUrl(candidateRequest.getResumeUrl())
                .skills(candidateRequest.getSkills())
                .educations(candidateRequest.getEducations())
                .experiences(candidateRequest.getExperiences())
                .build();

        Candidate createdCandidate = candidateService.createCandidate(candidate, user.getId());
        CandidateResponse response = buildCandidateResponse(createdCandidate, user);

        return ResponseEntity.ok(new ApiResponse<>(true, "Candidate profile created successfully", response));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> getCurrentCandidateProfile() {
        String email = authenticationFacade.getCurrentUsername();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Candidate candidate = candidateService.getCandidateByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Candidate profile not found"));

        CandidateResponse response = buildCandidateResponse(candidate, user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Candidate profile retrieved successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCandidateById(@PathVariable String id) {
        Candidate candidate = candidateService.getCandidateById(id)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found with id: " + id));

        User user = userService.getUserById(candidate.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        CandidateResponse response = buildCandidateResponse(candidate, user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Candidate profile retrieved successfully", response));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> updateCandidateProfile(@Valid @RequestBody CandidateRequest candidateRequest) {
        String email = authenticationFacade.getCurrentUsername();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Candidate candidate = candidateService.getCandidateByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Candidate profile not found"));

        candidate.setResumeSummary(candidateRequest.getResumeSummary());
        candidate.setPhoneNumber(candidateRequest.getPhoneNumber());
        candidate.setLinkedInProfile(candidateRequest.getLinkedInProfile());
        candidate.setGithubProfile(candidateRequest.getGithubProfile());
        candidate.setPortfolioUrl(candidateRequest.getPortfolioUrl());
        candidate.setResumeUrl(candidateRequest.getResumeUrl());
        candidate.setSkills(candidateRequest.getSkills());
        candidate.setEducations(candidateRequest.getEducations());
        candidate.setExperiences(candidateRequest.getExperiences());

        Candidate updatedCandidate = candidateService.updateCandidate(candidate);
        CandidateResponse response = buildCandidateResponse(updatedCandidate, user);

        return ResponseEntity.ok(new ApiResponse<>(true, "Candidate profile updated successfully", response));
    }

    private CandidateResponse buildCandidateResponse(Candidate candidate, User user) {
        long applicationsCount = applicationService.countApplicationsByCandidate(candidate.getId());

        return CandidateResponse.builder()
                .id(candidate.getId())
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .resumeSummary(candidate.getResumeSummary())
                .phoneNumber(candidate.getPhoneNumber())
                .linkedInProfile(candidate.getLinkedInProfile())
                .githubProfile(candidate.getGithubProfile())
                .portfolioUrl(candidate.getPortfolioUrl())
                .resumeUrl(candidate.getResumeUrl())
                .skills(candidate.getSkills())
                .educations(candidate.getEducations())
                .experiences(candidate.getExperiences())
                .applicationsCount(applicationsCount)
                .build();
    }
}