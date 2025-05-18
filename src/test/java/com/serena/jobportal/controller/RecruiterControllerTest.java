package com.serena.jobportal.controller;

import com.serena.jobportal.dto.request.RecruiterRequest;
import com.serena.jobportal.model.Recruiter;
import com.serena.jobportal.model.User;
import com.serena.jobportal.repository.JobRepository;
import com.serena.jobportal.security.AuthenticationFacade;
import com.serena.jobportal.service.RecruiterService;
import com.serena.jobportal.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class RecruiterControllerTest {

    @Mock
    private RecruiterService recruiterService;

    @Mock
    private UserService userService;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private RecruiterController recruiterController;

    private User testUser;
    private Recruiter testRecruiter;
    private RecruiterRequest recruiterRequest;

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
                .companyDescription("Leading software company")
                .position("Technical Recruiter")
                .build();

        recruiterRequest = new RecruiterRequest();
        recruiterRequest.setCompanyName("Tech Solutions Inc.");
        recruiterRequest.setCompanyDescription("Leading software company");
        recruiterRequest.setPosition("Technical Recruiter");
    }

    @Test
    void createRecruiterTest() {
        when(authenticationFacade.getCurrentUsername()).thenReturn("john.doe@example.com");
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(recruiterService.createRecruiter(any(Recruiter.class), anyString())).thenReturn(testRecruiter);
        when(jobRepository.countByRecruiterId(anyString())).thenReturn(0L);

        ResponseEntity<?> response = recruiterController.createRecruiter(recruiterRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Recruiter profile created successfully"));
    }

    @Test
    void getCurrentRecruiterProfileTest() {
        when(authenticationFacade.getCurrentUsername()).thenReturn("john.doe@example.com");
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(recruiterService.getRecruiterByUserId(anyString())).thenReturn(Optional.of(testRecruiter));
        when(jobRepository.countByRecruiterId(anyString())).thenReturn(0L);

        ResponseEntity<?> response = recruiterController.getCurrentRecruiterProfile();

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Recruiter profile retrieved successfully"));
    }

    @Test
    void getRecruiterByIdTest() {
        when(recruiterService.getRecruiterById(anyString())).thenReturn(Optional.of(testRecruiter));
        when(userService.getUserById(anyString())).thenReturn(Optional.of(testUser));
        when(jobRepository.countByRecruiterId(anyString())).thenReturn(0L);

        ResponseEntity<?> response = recruiterController.getRecruiterById("recruiterId");

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Recruiter profile retrieved successfully"));
    }

    @Test
    void updateRecruiterProfileTest() {
        when(authenticationFacade.getCurrentUsername()).thenReturn("john.doe@example.com");
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(recruiterService.getRecruiterByUserId(anyString())).thenReturn(Optional.of(testRecruiter));
        when(recruiterService.updateRecruiter(any(Recruiter.class))).thenReturn(testRecruiter);
        when(jobRepository.countByRecruiterId(anyString())).thenReturn(0L);

        ResponseEntity<?> response = recruiterController.updateRecruiterProfile(recruiterRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Recruiter profile updated successfully"));
    }
}