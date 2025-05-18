// JobServiceTest.java
package com.serena.jobportal.service;

import com.serena.jobportal.model.Job;
import com.serena.jobportal.model.Recruiter;
import com.serena.jobportal.model.User;
import com.serena.jobportal.repository.JobRepository;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JobServiceTest {

    @Mock
    private JobRepository jobRepository;

    @Mock
    private RecruiterService recruiterService;

    @InjectMocks
    private JobService jobService;

    private Recruiter testRecruiter;
    private Job testJob;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        User testUser = User.builder().id("userId").build();

        testRecruiter = Recruiter.builder()
                .id("recruiterId")
                .user(testUser)
                .companyName("TechCorp")
                .build();

        testJob = Job.builder()
                .id("jobId")
                .recruiter(testRecruiter)
                .title("Senior Java Developer")
                .description("Java developer position")
                .companyName("TechCorp")
                .location("San Francisco")
                .employmentType(Job.EmploymentType.FULL_TIME)
                .active(true)
                .build();

        pageable = PageRequest.of(0, 10);
    }

    @Test
    void createJob_Success() {
        when(recruiterService.getRecruiterById(anyString())).thenReturn(Optional.of(testRecruiter));
        when(jobRepository.save(any(Job.class))).thenReturn(testJob);

        Job result = jobService.createJob(testJob, "recruiterId");

        assertNotNull(result);
        assertEquals("jobId", result.getId());
        assertEquals(testRecruiter, result.getRecruiter());
        assertTrue(result.isActive());
        assertNotNull(result.getPostedAt());
        verify(jobRepository).save(testJob);
    }

    @Test
    void createJob_RecruiterNotFound_ThrowsException() {
        when(recruiterService.getRecruiterById(anyString())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
                jobService.createJob(testJob, "nonexistentId"));

        verify(jobRepository, never()).save(any(Job.class));
    }

    @Test
    void getJobById_Success() {
        when(jobRepository.findById(anyString())).thenReturn(Optional.of(testJob));

        Optional<Job> result = jobService.getJobById("jobId");

        assertTrue(result.isPresent());
        assertEquals("Senior Java Developer", result.get().getTitle());
    }

    @Test
    void getActiveJobs_Success() {
        Page<Job> jobPage = new PageImpl<>(Arrays.asList(testJob));
        when(jobRepository.findByActive(eq(true), any(Pageable.class))).thenReturn(jobPage);

        Page<Job> results = jobService.getActiveJobs(pageable);

        assertEquals(1, results.getTotalElements());
        assertEquals("Senior Java Developer", results.getContent().get(0).getTitle());
    }

    @Test
    void getJobsByRecruiter_Success() {
        Page<Job> jobPage = new PageImpl<>(Arrays.asList(testJob));
        when(jobRepository.findByRecruiter(any(Recruiter.class), any(Pageable.class))).thenReturn(jobPage);

        Page<Job> results = jobService.getJobsByRecruiter(testRecruiter, pageable);

        assertEquals(1, results.getTotalElements());
        assertEquals("Senior Java Developer", results.getContent().get(0).getTitle());
    }

    @Test
    void searchJobsByTitle_Success() {
        Page<Job> jobPage = new PageImpl<>(Arrays.asList(testJob));
        when(jobRepository.findByTitleContainingIgnoreCase(anyString(), any(Pageable.class))).thenReturn(jobPage);

        Page<Job> results = jobService.searchJobsByTitle("Java", pageable);

        assertEquals(1, results.getTotalElements());
        assertEquals("Senior Java Developer", results.getContent().get(0).getTitle());
    }

    @Test
    void searchJobsBySkills_Success() {
        Page<Job> jobPage = new PageImpl<>(Arrays.asList(testJob));
        when(jobRepository.findBySkillsInAndActive(anyList(), eq(true), any(Pageable.class))).thenReturn(jobPage);

        Page<Job> results = jobService.searchJobsBySkills(Arrays.asList("Java"), pageable);

        assertEquals(1, results.getTotalElements());
        assertEquals("Senior Java Developer", results.getContent().get(0).getTitle());
    }

    @Test
    void updateJob_Success() {
        when(jobRepository.save(any(Job.class))).thenReturn(testJob);

        Job result = jobService.updateJob(testJob);

        assertNotNull(result);
        assertEquals("jobId", result.getId());
        verify(jobRepository).save(testJob);
    }

    @Test
    void activateJob_Success() {
        testJob.setActive(false); // Initially inactive
        when(jobRepository.findById(anyString())).thenReturn(Optional.of(testJob));
        when(jobRepository.save(any(Job.class))).thenReturn(testJob);

        Job result = jobService.activateJob("jobId");

        assertTrue(result.isActive());
        verify(jobRepository).save(testJob);
    }

    @Test
    void deactivateJob_Success() {
        when(jobRepository.findById(anyString())).thenReturn(Optional.of(testJob));
        when(jobRepository.save(any(Job.class))).thenReturn(testJob);

        Job result = jobService.deactivateJob("jobId");

        assertFalse(result.isActive());
        verify(jobRepository).save(testJob);
    }

    @Test
    void deleteJob_Success() {
        doNothing().when(jobRepository).deleteById(anyString());

        jobService.deleteJob("jobId");

        verify(jobRepository).deleteById("jobId");
    }
}