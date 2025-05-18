// RecruiterServiceTest.java
package com.serena.jobportal.service;

import com.serena.jobportal.model.Recruiter;
import com.serena.jobportal.model.User;
import com.serena.jobportal.repository.RecruiterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RecruiterServiceTest {

    @Mock
    private RecruiterRepository recruiterRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private RecruiterService recruiterService;

    private User testUser;
    private Recruiter testRecruiter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = User.builder()
                .id("userId")
                .firstName("Robert")
                .lastName("Johnson")
                .email("robert.johnson@example.com")
                .build();

        testRecruiter = Recruiter.builder()
                .id("recruiterId")
                .user(testUser)
                .companyName("TechCorp")
                .position("Technical Recruiter")
                .build();
    }

    @Test
    void createRecruiter_Success() {
        when(userService.getUserById(anyString())).thenReturn(Optional.of(testUser));
        when(recruiterRepository.save(any(Recruiter.class))).thenReturn(testRecruiter);

        Recruiter result = recruiterService.createRecruiter(testRecruiter, "userId");

        assertNotNull(result);
        assertEquals("recruiterId", result.getId());
        assertEquals(testUser, result.getUser());
        verify(recruiterRepository).save(testRecruiter);
    }

    @Test
    void createRecruiter_UserNotFound_ThrowsException() {
        when(userService.getUserById(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                recruiterService.createRecruiter(testRecruiter, "nonexistentId"));

        verify(recruiterRepository, never()).save(any(Recruiter.class));
    }

    @Test
    void getRecruiterById_Success() {
        when(recruiterRepository.findById(anyString())).thenReturn(Optional.of(testRecruiter));

        Optional<Recruiter> result = recruiterService.getRecruiterById("recruiterId");

        assertTrue(result.isPresent());
        assertEquals("TechCorp", result.get().getCompanyName());
    }

    @Test
    void getRecruiterByUserId_Success() {
        when(recruiterRepository.findByUserId(anyString())).thenReturn(Optional.of(testRecruiter));

        Optional<Recruiter> result = recruiterService.getRecruiterByUserId("userId");

        assertTrue(result.isPresent());
        assertEquals("Technical Recruiter", result.get().getPosition());
    }

    @Test
    void getRecruitersByCompanyName_Success() {
        User secondUser = User.builder().id("userId2").build();
        Recruiter secondRecruiter = Recruiter.builder()
                .id("recruiterId2")
                .user(secondUser)
                .companyName("TechCorp")
                .position("Senior Recruiter")
                .build();

        when(recruiterRepository.findByCompanyName(anyString()))
                .thenReturn(Arrays.asList(testRecruiter, secondRecruiter));

        List<Recruiter> results = recruiterService.getRecruitersByCompanyName("TechCorp");

        assertEquals(2, results.size());
        assertEquals("Technical Recruiter", results.get(0).getPosition());
        assertEquals("Senior Recruiter", results.get(1).getPosition());
    }

    @Test
    void getAllRecruiters_Success() {
        User secondUser = User.builder().id("userId2").build();
        Recruiter secondRecruiter = Recruiter.builder()
                .id("recruiterId2")
                .user(secondUser)
                .companyName("AnotherCorp")
                .build();

        when(recruiterRepository.findAll()).thenReturn(Arrays.asList(testRecruiter, secondRecruiter));

        List<Recruiter> results = recruiterService.getAllRecruiters();

        assertEquals(2, results.size());
        assertEquals("TechCorp", results.get(0).getCompanyName());
        assertEquals("AnotherCorp", results.get(1).getCompanyName());
    }

    @Test
    void updateRecruiter_Success() {
        when(recruiterRepository.save(any(Recruiter.class))).thenReturn(testRecruiter);

        Recruiter result = recruiterService.updateRecruiter(testRecruiter);

        assertNotNull(result);
        assertEquals("recruiterId", result.getId());
        verify(recruiterRepository).save(testRecruiter);
    }

    @Test
    void deleteRecruiter_Success() {
        doNothing().when(recruiterRepository).deleteById(anyString());

        recruiterService.deleteRecruiter("recruiterId");

        verify(recruiterRepository).deleteById("recruiterId");
    }

    @Test
    void existsByUserId_True() {
        when(recruiterRepository.existsByUserId(anyString())).thenReturn(true);

        boolean result = recruiterService.existsByUserId("userId");

        assertTrue(result);
    }

    @Test
    void existsByUserId_False() {
        when(recruiterRepository.existsByUserId(anyString())).thenReturn(false);

        boolean result = recruiterService.existsByUserId("nonexistentId");

        assertFalse(result);
    }
}