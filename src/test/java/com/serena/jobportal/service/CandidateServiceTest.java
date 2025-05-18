// CandidateServiceTest.java
package com.serena.jobportal.service;

import com.serena.jobportal.model.Candidate;
import com.serena.jobportal.model.User;
import com.serena.jobportal.repository.CandidateRepository;
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

public class CandidateServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CandidateService candidateService;

    private User testUser;
    private Candidate testCandidate;

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
                .resumeSummary("Java developer with 5 years experience")
                .phoneNumber("555-123-4567")
                .build();
    }

    @Test
    void createCandidate_Success() {
        when(userService.getUserById(anyString())).thenReturn(Optional.of(testUser));
        when(candidateRepository.save(any(Candidate.class))).thenReturn(testCandidate);

        Candidate result = candidateService.createCandidate(testCandidate, "userId");

        assertNotNull(result);
        assertEquals("candidateId", result.getId());
        assertEquals(testUser, result.getUser());
        verify(candidateRepository).save(testCandidate);
    }

    @Test
    void createCandidate_UserNotFound_ThrowsException() {
        when(userService.getUserById(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                candidateService.createCandidate(testCandidate, "nonexistentId"));

        verify(candidateRepository, never()).save(any(Candidate.class));
    }

    @Test
    void getCandidateById_Success() {
        when(candidateRepository.findById(anyString())).thenReturn(Optional.of(testCandidate));

        Optional<Candidate> result = candidateService.getCandidateById("candidateId");

        assertTrue(result.isPresent());
        assertEquals("Java developer with 5 years experience", result.get().getResumeSummary());
    }

    @Test
    void getCandidateByUserId_Success() {
        when(candidateRepository.findByUserId(anyString())).thenReturn(Optional.of(testCandidate));

        Optional<Candidate> result = candidateService.getCandidateByUserId("userId");

        assertTrue(result.isPresent());
        assertEquals("555-123-4567", result.get().getPhoneNumber());
    }

    @Test
    void getAllCandidates_Success() {
        User secondUser = User.builder().id("userId2").build();
        Candidate secondCandidate = Candidate.builder()
                .id("candidateId2")
                .user(secondUser)
                .resumeSummary("Frontend developer")
                .build();

        when(candidateRepository.findAll()).thenReturn(Arrays.asList(testCandidate, secondCandidate));

        List<Candidate> results = candidateService.getAllCandidates();

        assertEquals(2, results.size());
        assertEquals("Java developer with 5 years experience", results.get(0).getResumeSummary());
        assertEquals("Frontend developer", results.get(1).getResumeSummary());
    }

    @Test
    void updateCandidate_Success() {
        when(candidateRepository.save(any(Candidate.class))).thenReturn(testCandidate);

        Candidate result = candidateService.updateCandidate(testCandidate);

        assertNotNull(result);
        assertEquals("candidateId", result.getId());
        verify(candidateRepository).save(testCandidate);
    }

    @Test
    void deleteCandidate_Success() {
        doNothing().when(candidateRepository).deleteById(anyString());

        candidateService.deleteCandidate("candidateId");

        verify(candidateRepository).deleteById("candidateId");
    }

    @Test
    void existsByUserId_True() {
        when(candidateRepository.existsByUserId(anyString())).thenReturn(true);

        boolean result = candidateService.existsByUserId("userId");

        assertTrue(result);
    }

    @Test
    void existsByUserId_False() {
        when(candidateRepository.existsByUserId(anyString())).thenReturn(false);

        boolean result = candidateService.existsByUserId("nonexistentId");

        assertFalse(result);
    }
}