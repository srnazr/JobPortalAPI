// ApplicationController.java (continued)
package com.serena.jobportal.controller;

import com.serena.jobportal.dto.request.ApplicationRequest;
import com.serena.jobportal.dto.response.ApplicationResponse;
import com.serena.jobportal.model.Application;
import com.serena.jobportal.model.Candidate;
import com.serena.jobportal.model.User;
import com.serena.jobportal.security.AuthenticationFacade;
import com.serena.jobportal.service.ApplicationService;
import com.serena.jobportal.service.CandidateService;
import com.serena.jobportal.service.JobService;
import com.serena.jobportal.service.UserService;
import com.serena.jobportal.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final JobService jobService;
    private final CandidateService candidateService;
    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;

    @PostMapping
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> applyForJob(@Valid @RequestBody ApplicationRequest applicationRequest) {
        String email = authenticationFacade.getCurrentUsername();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Candidate candidate = candidateService.getCandidateByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Candidate profile not found"));

        Application application = Application.builder()
                .coverLetter(applicationRequest.getCoverLetter())
                .resumeUrl(applicationRequest.getResumeUrl())
                .build();

        Application createdApplication = applicationService.createApplication(
                application, applicationRequest.getJobId(), candidate.getId());

        ApplicationResponse response = buildApplicationResponse(createdApplication);

        return ResponseEntity.ok(new ApiResponse<>(true, "Application submitted successfully", response));
    }

    @GetMapping("/candidate")
    @PreAuthorize("hasRole('CANDIDATE')")
    public ResponseEntity<?> getCandidateApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String email = authenticationFacade.getCurrentUsername();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Candidate candidate = candidateService.getCandidateByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Candidate profile not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        Page<Application> applicationsPage = applicationService.getApplicationsByCandidate(candidate.getId(), pageable);

        List<ApplicationResponse> applicationResponses = applicationsPage.getContent().stream()
                .map(this::buildApplicationResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Applications retrieved successfully", applicationResponses));
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getJobApplications(
            @PathVariable String jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Verify job exists
        jobService.getJobById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + jobId));

        Pageable pageable = PageRequest.of(page, size, Sort.by("appliedAt").descending());
        Page<Application> applicationsPage = applicationService.getApplicationsByJob(jobId, pageable);

        List<ApplicationResponse> applicationResponses = applicationsPage.getContent().stream()
                .map(this::buildApplicationResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Applications retrieved successfully", applicationResponses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getApplicationById(@PathVariable String id) {
        Application application = applicationService.getApplicationById(id)
                .orElseThrow(() -> new IllegalArgumentException("Application not found with id: " + id));

        ApplicationResponse applicationResponse = buildApplicationResponse(application);

        return ResponseEntity.ok(new ApiResponse<>(true, "Application retrieved successfully", applicationResponse));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> updateApplicationStatus(
            @PathVariable String id,
            @RequestParam Application.Status status) {

        Application updatedApplication = applicationService.updateApplicationStatus(id, status);
        ApplicationResponse applicationResponse = buildApplicationResponse(updatedApplication);

        return ResponseEntity.ok(new ApiResponse<>(true, "Application status updated successfully", applicationResponse));
    }

    private ApplicationResponse buildApplicationResponse(Application application) {
        return ApplicationResponse.builder()
                .id(application.getId())
                .jobId(application.getJob().getId())
                .jobTitle(application.getJob().getTitle())
                .companyName(application.getJob().getCompanyName())
                .candidateId(application.getCandidate().getId())
                .candidateName(application.getCandidate().getUser().getFirstName() + " " +
                        application.getCandidate().getUser().getLastName())
                .coverLetter(application.getCoverLetter())
                .resumeUrl(application.getResumeUrl())
                .status(application.getStatus())
                .appliedAt(application.getAppliedAt())
                .updatedAt(application.getUpdatedAt())
                .notes(application.getNotes())
                .build();
    }
}