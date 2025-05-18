// JobControllerTest.java
package com.serena.jobportal.controller;

import com.serena.jobportal.dto.request.JobRequest;
import com.serena.jobportal.dto.request.JobSearchRequest;
import com.serena.jobportal.model.Job;
import com.serena.jobportal.model.Recruiter;
import com.serena.jobportal.model.User;
import com.serena.jobportal.security.AuthenticationFacade;
import com.serena.jobportal.service.ApplicationService;
import com.serena.jobportal.service.JobService;
import com.serena.jobportal.service.RecruiterService;
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

public class JobControllerTest {

    @Mock
    private JobService jobService;

    @Mock
    private RecruiterService recruiterService;

    @Mock
    private UserService userService;

    @Mock
    private ApplicationService applicationService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private JobController jobController;

    private User testUser;
    private Recruiter testRecruiter;
    private Job testJob;
    private JobRequest jobRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = User.builder()
                .id("userId")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        testRecruiter = Recruiter.builder()
                .id("recruiterId")
                .user(testUser)
                .companyName("Tech Solutions Inc.")
                .build();

        testJob = Job.builder()
                .id("jobId")
                .recruiter(testRecruiter)
                .title("Java Developer")
                .description("Java developer position")
                .companyName("Tech Solutions Inc.")
                .location("San Francisco, CA")
                .employmentType(Job.EmploymentType.FULL_TIME)
                .active(true)
                .build();

        jobRequest = new JobRequest();
        jobRequest.setTitle("Java Developer");
        jobRequest.setDescription("Java developer position");
        jobRequest.setCompanyName("Tech Solutions Inc.");
        jobRequest.setLocation("San Francisco, CA");
        jobRequest.setEmploymentType(Job.EmploymentType.FULL_TIME);
    }

    @Test
    void createJobTest() {
        when(authenticationFacade.getCurrentUsername()).thenReturn("john.doe@example.com");
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(recruiterService.getRecruiterByUserId(anyString())).thenReturn(Optional.of(testRecruiter));
        when(jobService.createJob(any(Job.class), anyString())).thenReturn(testJob);
        when(applicationService.countApplicationsByJob(anyString())).thenReturn(0L);

        ResponseEntity<?> response = jobController.createJob(jobRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Job created successfully"));
    }

    @Test
    void getJobByIdTest() {
        when(jobService.getJobById(anyString())).thenReturn(Optional.of(testJob));
        when(applicationService.countApplicationsByJob(anyString())).thenReturn(0L);

        ResponseEntity<?> response = jobController.getJobById("jobId");

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Job retrieved successfully"));
    }

    @Test
    void getActiveJobsTest() {
        Page<Job> jobPage = new PageImpl<>(Arrays.asList(testJob));
        when(jobService.getActiveJobs(any(Pageable.class))).thenReturn(jobPage);
        when(applicationService.countApplicationsByJob(anyString())).thenReturn(0L);

        ResponseEntity<?> response = jobController.getActiveJobs(0, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Jobs retrieved successfully"));
    }

    @Test
    void getRecruiterJobsTest() {
        when(authenticationFacade.getCurrentUsername()).thenReturn("john.doe@example.com");
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(recruiterService.getRecruiterByUserId(anyString())).thenReturn(Optional.of(testRecruiter));

        Page<Job> jobPage = new PageImpl<>(Arrays.asList(testJob));
        when(jobService.getJobsByRecruiter(any(Recruiter.class), any(Pageable.class))).thenReturn(jobPage);
        when(applicationService.countApplicationsByJob(anyString())).thenReturn(0L);

        ResponseEntity<?> response = jobController.getRecruiterJobs(0, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Recruiter jobs retrieved successfully"));
    }

    @Test
    void searchJobsTest() {
        JobSearchRequest searchRequest = new JobSearchRequest();
        searchRequest.setKeyword("Java");

        Page<Job> jobPage = new PageImpl<>(Arrays.asList(testJob));
        when(jobService.searchJobs(anyString(), any(Pageable.class))).thenReturn(jobPage);
        when(applicationService.countApplicationsByJob(anyString())).thenReturn(0L);

        ResponseEntity<?> response = jobController.searchJobs(searchRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Jobs search results"));
    }

    @Test
    void updateJobTest() {
        when(authenticationFacade.getCurrentUsername()).thenReturn("john.doe@example.com");
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(recruiterService.getRecruiterByUserId(anyString())).thenReturn(Optional.of(testRecruiter));
        when(jobService.getJobById(anyString())).thenReturn(Optional.of(testJob));
        when(jobService.updateJob(any(Job.class))).thenReturn(testJob);
        when(applicationService.countApplicationsByJob(anyString())).thenReturn(0L);

        ResponseEntity<?> response = jobController.updateJob("jobId", jobRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Job updated successfully"));
    }

    @Test
    void activateJobTest() {
        when(authenticationFacade.getCurrentUsername()).thenReturn("john.doe@example.com");
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(recruiterService.getRecruiterByUserId(anyString())).thenReturn(Optional.of(testRecruiter));
        when(jobService.getJobById(anyString())).thenReturn(Optional.of(testJob));
        when(jobService.activateJob(anyString())).thenReturn(testJob);
        when(applicationService.countApplicationsByJob(anyString())).thenReturn(0L);

        ResponseEntity<?> response = jobController.activateJob("jobId");

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Job activated successfully"));
    }
}