// RecruiterController.java
package com.serena.jobportal.controller;

import com.serena.jobportal.dto.request.RecruiterRequest;
import com.serena.jobportal.dto.response.RecruiterResponse;
import com.serena.jobportal.model.Recruiter;
import com.serena.jobportal.model.User;
import com.serena.jobportal.repository.JobRepository;
import com.serena.jobportal.security.AuthenticationFacade;
import com.serena.jobportal.service.RecruiterService;
import com.serena.jobportal.service.UserService;
import com.serena.jobportal.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiters")
@RequiredArgsConstructor
public class RecruiterController {

    private final RecruiterService recruiterService;
    private final UserService userService;
    private final JobRepository jobRepository;
    private final AuthenticationFacade authenticationFacade;

    @PostMapping
    public ResponseEntity<?> createRecruiter(@Valid @RequestBody RecruiterRequest recruiterRequest) {
        String email = authenticationFacade.getCurrentUsername();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Recruiter recruiter = Recruiter.builder()
                .companyName(recruiterRequest.getCompanyName())
                .companyDescription(recruiterRequest.getCompanyDescription())
                .companyWebsite(recruiterRequest.getCompanyWebsite())
                .phoneNumber(recruiterRequest.getPhoneNumber())
                .position(recruiterRequest.getPosition())
                .linkedInProfile(recruiterRequest.getLinkedInProfile())
                .build();

        Recruiter createdRecruiter = recruiterService.createRecruiter(recruiter, user.getId());
        RecruiterResponse response = buildRecruiterResponse(createdRecruiter, user);

        return ResponseEntity.ok(new ApiResponse<>(true, "Recruiter profile created successfully", response));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getCurrentRecruiterProfile() {
        String email = authenticationFacade.getCurrentUsername();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Recruiter recruiter = recruiterService.getRecruiterByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Recruiter profile not found"));

        RecruiterResponse response = buildRecruiterResponse(recruiter, user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Recruiter profile retrieved successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecruiterById(@PathVariable String id) {
        Recruiter recruiter = recruiterService.getRecruiterById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recruiter not found with id: " + id));

        User user = userService.getUserById(recruiter.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        RecruiterResponse response = buildRecruiterResponse(recruiter, user);
        return ResponseEntity.ok(new ApiResponse<>(true, "Recruiter profile retrieved successfully", response));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> updateRecruiterProfile(@Valid @RequestBody RecruiterRequest recruiterRequest) {
        String email = authenticationFacade.getCurrentUsername();
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Recruiter recruiter = recruiterService.getRecruiterByUserId(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Recruiter profile not found"));

        recruiter.setCompanyName(recruiterRequest.getCompanyName());
        recruiter.setCompanyDescription(recruiterRequest.getCompanyDescription());
        recruiter.setCompanyWebsite(recruiterRequest.getCompanyWebsite());
        recruiter.setPhoneNumber(recruiterRequest.getPhoneNumber());
        recruiter.setPosition(recruiterRequest.getPosition());
        recruiter.setLinkedInProfile(recruiterRequest.getLinkedInProfile());

        Recruiter updatedRecruiter = recruiterService.updateRecruiter(recruiter);
        RecruiterResponse response = buildRecruiterResponse(updatedRecruiter, user);

        return ResponseEntity.ok(new ApiResponse<>(true, "Recruiter profile updated successfully", response));
    }

    private RecruiterResponse buildRecruiterResponse(Recruiter recruiter, User user) {
        long jobsCount = jobRepository.countByRecruiterId(recruiter.getId());

        return RecruiterResponse.builder()
                .id(recruiter.getId())
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .companyName(recruiter.getCompanyName())
                .companyDescription(recruiter.getCompanyDescription())
                .companyWebsite(recruiter.getCompanyWebsite())
                .phoneNumber(recruiter.getPhoneNumber())
                .position(recruiter.getPosition())
                .linkedInProfile(recruiter.getLinkedInProfile())
                .jobsCount(jobsCount)
                .build();
    }
}