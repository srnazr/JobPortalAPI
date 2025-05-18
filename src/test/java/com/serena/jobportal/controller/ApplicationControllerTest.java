package com.serena.jobportal.controller;

import com.serena.jobportal.dto.request.ApplicationRequest;
import com.serena.jobportal.model.Application;
import com.serena.jobportal.model.Candidate;
import com.serena.jobportal.model.Job;
import com.serena.jobportal.model.User;
import com.serena.jobportal.security.AuthenticationFacade;
import com.serena.jobportal.service.ApplicationService;
import com.serena.jobportal.service.CandidateService;
import com.serena.jobportal.service.JobService;
import com.serena.jobportal.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ApplicationControllerTest {

    @Mock
    private ApplicationService applicationService;

    @Mock
    private JobService jobService;

    @Mock
    private CandidateService candidateService;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private ApplicationController applicationController;

    private User testUser;
    private Candidate testCandidate;
    private Job testJob;
    private Application testApplication;
    private ApplicationRequest applicationRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = User.builder()
                .id("userId")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        testCandidate = Candidate.builder()
                .id("candidateId")
                .user(testUser)
                .build();

        testJob = Job.builder()
                .id("jobId")
                .title("Java Developer")
                .build();

        testApplication = Application.builder()
                .id("applicationId")
                .job(testJob)
                .candidate(testCandidate)
                .coverLetter("I am interested in this position")
                .status(Application.Status.APPLIED)
                .build();

        applicationRequest = new ApplicationRequest();
        applicationRequest.setJobId("jobId");
        applicationRequest.setCoverLetter("I am interested in this position");
    }

    @Test
    void applyForJobTest() {
        when(authenticationFacade.getCurrentUsername()).thenReturn("john.doe@example.com");
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(candidateService.getCandidateByUserId(anyString())).thenReturn(Optional.of(testCandidate));
        when(applicationService.createApplication(any(Application.class), anyString(), anyString()))
                .thenReturn(testApplication);

        ResponseEntity<?> response = applicationController.applyForJob(applicationRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Application submitted successfully"));
    }

    @Test
    void getCandidateApplicationsTest() {
        when(authenticationFacade.getCurrentUsername()).thenReturn("john.doe@example.com");
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(candidateService.getCandidateByUserId(anyString())).thenReturn(Optional.of(testCandidate));

        Page<Application> applicationPage = new PageImpl<>(Arrays.asList(testApplication));
        when(applicationService.getApplicationsByCandidate(anyString(), any(Pageable.class)))
                .thenReturn(applicationPage);

        ResponseEntity<?> response = applicationController.getCandidateApplications(0, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Applications retrieved successfully"));
    }

    @Test
    void getApplicationByIdTest() {
        when(applicationService.getApplicationById(anyString())).thenReturn(Optional.of(testApplication));

        ResponseEntity<?> response = applicationController.getApplicationById("applicationId");

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Application retrieved successfully"));
    }
}