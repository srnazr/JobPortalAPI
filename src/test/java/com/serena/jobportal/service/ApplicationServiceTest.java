// ApplicationServiceTest.java
package com.serena.jobportal.service;

import com.serena.jobportal.model.Application;
import com.serena.jobportal.model.Candidate;
import com.serena.jobportal.model.Job;
import com.serena.jobportal.repository.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private JobService jobService;

    @Mock
    private CandidateService candidateService;

    @InjectMocks
    private ApplicationService applicationService;

    private Job testJob;
    private Candidate testCandidate;
    private Application testApplication;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testJob = Job.builder()
                .id("jobId")
                .title("Java Developer")
                .build();

        testCandidate = Candidate.builder()
                .id("candidateId")
                .resumeSummary("Java developer with experience")
                .build();

        testApplication = Application.builder()
                .id("applicationId")
                .job(testJob)
                .candidate(testCandidate)
                .status(Application.Status.APPLIED)
                .coverLetter("I am interested in this position")
                .build();

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void createApplication_Success() {
        when(applicationRepository.existsByJobIdAndCandidateId(anyString(), anyString())).thenReturn(false);
        when(jobService.getJobById(anyString())).thenReturn(Optional.of(testJob));
        when(candidateService.getCandidateById(anyString())).thenReturn(Optional.of(testCandidate));
        when(applicationRepository.save(any(Application.class))).thenReturn(testApplication);

        Application result = applicationService.createApplication(testApplication, "jobId", "candidateId");

        assertNotNull(result);
        assertEquals("applicationId", result.getId());
        assertEquals(testJob, result.getJob());
        assertEquals(testCandidate, result.getCandidate());
        assertEquals(Application.Status.APPLIED, result.getStatus());
        assertNotNull(result.getAppliedAt());
        verify(applicationRepository).save(testApplication);
    }

    @Test
    void createApplication_AlreadyApplied_ThrowsException() {
        when(applicationRepository.existsByJobIdAndCandidateId(anyString(), anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                applicationService.createApplication(testApplication, "jobId", "candidateId"));

        verify(applicationRepository, never()).save(any(Application.class));
    }

    @Test
    void createApplication_JobNotFound_ThrowsException() {
        when(applicationRepository.existsByJobIdAndCandidateId(anyString(), anyString())).thenReturn(false);
        when(jobService.getJobById(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                applicationService.createApplication(testApplication, "nonexistentId", "candidateId"));

        verify(applicationRepository, never()).save(any(Application.class));
    }

    @Test
    void createApplication_CandidateNotFound_ThrowsException() {
        when(applicationRepository.existsByJobIdAndCandidateId(anyString(), anyString())).thenReturn(false);
        when(jobService.getJobById(anyString())).thenReturn(Optional.of(testJob));
        when(candidateService.getCandidateById(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                applicationService.createApplication(testApplication, "jobId", "nonexistentId"));

        verify(applicationRepository, never()).save(any(Application.class));
    }

    @Test
    void getApplicationById_Success() {
        when(applicationRepository.findById(anyString())).thenReturn(Optional.of(testApplication));

        Optional<Application> result = applicationService.getApplicationById("applicationId");

        assertTrue(result.isPresent());
        assertEquals("I am interested in this position", result.get().getCoverLetter());
    }

    @Test
    void getApplicationsByJob_Success() {
        Page<Application> applicationPage = new PageImpl<>(Arrays.asList(testApplication));
        when(jobService.getJobById(anyString())).thenReturn(Optional.of(testJob));
        when(applicationRepository.findByJob(any(Job.class), any(Pageable.class))).thenReturn(applicationPage);

        Page<Application> results = applicationService.getApplicationsByJob("jobId", pageable);

        assertEquals(1, results.getTotalElements());
        assertEquals("applicationId", results.getContent().get(0).getId());
    }

    @Test
    void getApplicationsByCandidate_Success() {
        Page<Application> applicationPage = new PageImpl<>(Arrays.asList(testApplication));
        when(candidateService.getCandidateById(anyString())).thenReturn(Optional.of(testCandidate));
        when(applicationRepository.findByCandidate(any(Candidate.class), any(Pageable.class))).thenReturn(applicationPage);

        Page<Application> results = applicationService.getApplicationsByCandidate("candidateId", pageable);

        assertEquals(1, results.getTotalElements());
        assertEquals("applicationId", results.getContent().get(0).getId());
    }

    @Test
    void getApplicationsByJobAndStatus_Success() {
        Page<Application> applicationPage = new PageImpl<>(Arrays.asList(testApplication));
        when(applicationRepository.findByJobIdAndStatus(anyString(), any(Application.Status.class), any(Pageable.class)))
                .thenReturn(applicationPage);

        Page<Application> results = applicationService.getApplicationsByJobAndStatus(
                "jobId", Application.Status.APPLIED, pageable);

        assertEquals(1, results.getTotalElements());
        assertEquals("applicationId", results.getContent().get(0).getId());
    }

    @Test
    void updateApplicationStatus_Success() {
        when(applicationRepository.findById(anyString())).thenReturn(Optional.of(testApplication));
        when(applicationRepository.save(any(Application.class))).thenReturn(testApplication);

        Application result = applicationService.updateApplicationStatus("applicationId", Application.Status.SHORTLISTED);

        assertEquals(Application.Status.SHORTLISTED, result.getStatus());
        assertNotNull(result.getUpdatedAt());
        verify(applicationRepository).save(testApplication);
    }

    @Test
    void updateApplication_Success() {
        when(applicationRepository.save(any(Application.class))).thenReturn(testApplication);

        Application result = applicationService.updateApplication(testApplication);

        assertNotNull(result);
        assertEquals("applicationId", result.getId());
        assertNotNull(result.getUpdatedAt());
        verify(applicationRepository).save(testApplication);
    }

    @Test
    void deleteApplication_Success() {
        doNothing().when(applicationRepository).deleteById(anyString());

        applicationService.deleteApplication("applicationId");

        verify(applicationRepository).deleteById("applicationId");
    }

    @Test
    void countApplicationsByJob_Success() {
        when(applicationRepository.countByJobId(anyString())).thenReturn(5L);

        long count = applicationService.countApplicationsByJob("jobId");

        assertEquals(5L, count);
    }

    @Test
    void countApplicationsByCandidate_Success() {
        when(applicationRepository.countByCandidateId(anyString())).thenReturn(3L);

        long count = applicationService.countApplicationsByCandidate("candidateId");

        assertEquals(3L, count);
    }
}