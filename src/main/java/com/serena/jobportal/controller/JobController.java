// JobController.java
package com.serena.jobportal.controller;

import com.serena.jobportal.dto.request.JobRequest;
import com.serena.jobportal.dto.request.JobSearchRequest;
import com.serena.jobportal.dto.response.JobResponse;
import com.serena.jobportal.model.Job;
import com.serena.jobportal.model.Recruiter;
import com.serena.jobportal.model.User;
import com.serena.jobportal.security.AuthenticationFacade;
import com.serena.jobportal.service.ApplicationService;
import com.serena.jobportal.service.JobService;
import com.serena.jobportal.service.RecruiterService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;
    private final RecruiterService recruiterService;
    private final UserService userService;
    private final ApplicationService applicationService;
    private final AuthenticationFacade authenticationFacade;

    @PostMapping
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> createJob(@Valid @RequestBody JobRequest jobRequest) {
        String email = authenticationFacade.getCurrentUsername();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Recruiter recruiter = recruiterService.getRecruiterByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Recruiter profile not found"));

        Job job = Job.builder()
                .title(jobRequest.getTitle())
                .description(jobRequest.getDescription())
                .companyName(jobRequest.getCompanyName())
                .location(jobRequest.getLocation())
                .remote(jobRequest.isRemote())
                .employmentType(jobRequest.getEmploymentType())
                .salary(jobRequest.getSalary())
                .requirements(jobRequest.getRequirements())
                .responsibilities(jobRequest.getResponsibilities())
                .skills(jobRequest.getSkills())
                .experienceLevel(jobRequest.getExperienceLevel())
                .deadline(jobRequest.getDeadline())
                .build();

        Job createdJob = jobService.createJob(job, recruiter.getId());
        JobResponse jobResponse = buildJobResponse(createdJob);

        return ResponseEntity.ok(new ApiResponse<>(true, "Job created successfully", jobResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable String id) {
        Job job = jobService.getJobById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + id));

        JobResponse jobResponse = buildJobResponse(job);
        return ResponseEntity.ok(new ApiResponse<>(true, "Job retrieved successfully", jobResponse));
    }

    @GetMapping
    public ResponseEntity<?> getActiveJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("postedAt").descending());
        Page<Job> jobsPage = jobService.getActiveJobs(pageable);

        List<JobResponse> jobResponses = jobsPage.getContent().stream()
                .map(this::buildJobResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Jobs retrieved successfully", jobResponses));
    }

    @GetMapping("/recruiter")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getRecruiterJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String email = authenticationFacade.getCurrentUsername();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Recruiter recruiter = recruiterService.getRecruiterByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Recruiter profile not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("postedAt").descending());
        Page<Job> jobsPage = jobService.getJobsByRecruiter(recruiter, pageable);

        List<JobResponse> jobResponses = jobsPage.getContent().stream()
                .map(this::buildJobResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Recruiter jobs retrieved successfully", jobResponses));
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchJobs(@RequestBody JobSearchRequest searchRequest) {
        Pageable pageable = PageRequest.of(
                searchRequest.getPagination() != null ? searchRequest.getPagination().getPage() : 0,
                searchRequest.getPagination() != null ? searchRequest.getPagination().getSize() : 10,
                Sort.by("postedAt").descending()
        );

        Page<Job> jobsPage;

        if (searchRequest.getKeyword() != null && !searchRequest.getKeyword().isEmpty()) {
            jobsPage = jobService.searchJobs(searchRequest.getKeyword(), pageable);
        } else if (searchRequest.getSkills() != null && !searchRequest.getSkills().isEmpty()) {
            jobsPage = jobService.searchJobsBySkills(searchRequest.getSkills(), pageable);
        } else {
            jobsPage = jobService.getActiveJobs(pageable);
        }

        List<JobResponse> jobResponses = jobsPage.getContent().stream()
                .map(this::buildJobResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ApiResponse<>(true, "Jobs search results", jobResponses));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> updateJob(@PathVariable String id, @Valid @RequestBody JobRequest jobRequest) {
        String email = authenticationFacade.getCurrentUsername();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Recruiter recruiter = recruiterService.getRecruiterByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Recruiter profile not found"));

        Job job = jobService.getJobById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + id));

        // Verify that the recruiter owns this job
        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            return ResponseEntity.status(403)
                    .body(new ApiResponse<>(false, "You don't have permission to update this job", null));
        }

        job.setTitle(jobRequest.getTitle());
        job.setDescription(jobRequest.getDescription());
        job.setCompanyName(jobRequest.getCompanyName());
        job.setLocation(jobRequest.getLocation());
        job.setRemote(jobRequest.isRemote());
        job.setEmploymentType(jobRequest.getEmploymentType());
        job.setSalary(jobRequest.getSalary());
        job.setRequirements(jobRequest.getRequirements());
        job.setResponsibilities(jobRequest.getResponsibilities());
        job.setSkills(jobRequest.getSkills());
        job.setExperienceLevel(jobRequest.getExperienceLevel());
        job.setDeadline(jobRequest.getDeadline());

        Job updatedJob = jobService.updateJob(job);
        JobResponse jobResponse = buildJobResponse(updatedJob);

        return ResponseEntity.ok(new ApiResponse<>(true, "Job updated successfully", jobResponse));
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> activateJob(@PathVariable String id) {
        verifyRecruiterOwnsJob(id);

        Job activatedJob = jobService.activateJob(id);
        JobResponse jobResponse = buildJobResponse(activatedJob);

        return ResponseEntity.ok(new ApiResponse<>(true, "Job activated successfully", jobResponse));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> deactivateJob(@PathVariable String id) {
        verifyRecruiterOwnsJob(id);

        Job deactivatedJob = jobService.deactivateJob(id);
        JobResponse jobResponse = buildJobResponse(deactivatedJob);

        return ResponseEntity.ok(new ApiResponse<>(true, "Job deactivated successfully", jobResponse));
    }

    private void verifyRecruiterOwnsJob(String jobId) {
        String email = authenticationFacade.getCurrentUsername();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Recruiter recruiter = recruiterService.getRecruiterByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Recruiter profile not found"));

        Job job = jobService.getJobById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found with id: " + jobId));

        if (!job.getRecruiter().getId().equals(recruiter.getId())) {
            throw new IllegalArgumentException("You don't have permission to modify this job");
        }
    }

    private JobResponse buildJobResponse(Job job) {
        long applicationsCount = applicationService.countApplicationsByJob(job.getId());

        return JobResponse.builder()
                .id(job.getId())
                .recruiterId(job.getRecruiter().getId())
                .recruiterName(job.getRecruiter().getUser().getFirstName() + " " + job.getRecruiter().getUser().getLastName())
                .title(job.getTitle())
                .description(job.getDescription())
                .companyName(job.getCompanyName())
                .location(job.getLocation())
                .remote(job.isRemote())
                .employmentType(job.getEmploymentType())
                .salary(job.getSalary())
                .requirements(job.getRequirements())
                .responsibilities(job.getResponsibilities())
                .skills(job.getSkills())
                .experienceLevel(job.getExperienceLevel())
                .postedAt(job.getPostedAt())
                .deadline(job.getDeadline())
                .active(job.isActive())
                .applicationsCount(applicationsCount)
                .build();
    }
}