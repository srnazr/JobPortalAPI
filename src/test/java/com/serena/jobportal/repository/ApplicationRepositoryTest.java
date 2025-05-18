// ApplicationRepositoryTest.java
package com.serena.jobportal.repository;

import com.serena.jobportal.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class ApplicationRepositoryTest {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private JobRepository jobRepository;

    private Candidate testCandidate;
    private Job testJob;
    private Application testApplication;

    @BeforeEach
    void setUp() {
        User candidateUser = User.builder()
                .firstName("Alice")
                .lastName("Brown")
                .email("alice.brown@example.com")
                .password("password123")
                .active(true)
                .build();
        candidateUser = userRepository.save(candidateUser);

        testCandidate = Candidate.builder()
                .user(candidateUser)
                .resumeSummary("Java developer with 3 years experience")
                .skills(Arrays.asList("Java", "Spring", "MongoDB"))
                .build();
        testCandidate = candidateRepository.save(testCandidate);

        User recruiterUser = User.builder()
                .firstName("David")
                .lastName("Green")
                .email("david.green@example.com")
                .password("password456")
                .active(true)
                .build();
        recruiterUser = userRepository.save(recruiterUser);

        Recruiter testRecruiter = Recruiter.builder()
                .user(recruiterUser)
                .companyName("XYZ Software")
                .build();
        testRecruiter = recruiterRepository.save(testRecruiter);

        testJob = Job.builder()
                .recruiter(testRecruiter)
                .title("Java Developer")
                .description("Java developer position...")
                .companyName("XYZ Software")
                .location("Chicago, IL")
                .employmentType(Job.EmploymentType.FULL_TIME)
                .active(true)
                .build();
        testJob = jobRepository.save(testJob);

        testApplication = Application.builder()
                .job(testJob)
                .candidate(testCandidate)
                .coverLetter("I am very interested in this position...")
                .status(Application.Status.APPLIED)
                .appliedAt(LocalDateTime.now())
                .build();
    }

    @AfterEach
    void tearDown() {
        applicationRepository.deleteAll();
        jobRepository.deleteAll();
        candidateRepository.deleteAll();
        recruiterRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSaveApplication() {
        Application savedApplication = applicationRepository.save(testApplication);

        assertNotNull(savedApplication);
        assertNotNull(savedApplication.getId());
        assertEquals(testJob.getId(), savedApplication.getJob().getId());
        assertEquals(testCandidate.getId(), savedApplication.getCandidate().getId());
    }

    @Test
    void testFindByJobAndStatus() {
        applicationRepository.save(testApplication);

        List<Application> applications = applicationRepository.findByJobAndStatus(testJob, Application.Status.APPLIED);

        assertEquals(1, applications.size());
        assertEquals(testApplication.getId(), applications.get(0).getId());
    }

    @Test
    void testFindByJob() {
        applicationRepository.save(testApplication);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Application> applications = applicationRepository.findByJob(testJob, pageable);

        assertEquals(1, applications.getTotalElements());
    }

    @Test
    void testFindByCandidate() {
        applicationRepository.save(testApplication);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Application> applications = applicationRepository.findByCandidate(testCandidate, pageable);

        assertEquals(1, applications.getTotalElements());
    }

    @Test
    void testFindByJobAndCandidate() {
        applicationRepository.save(testApplication);

        List<Application> applications = applicationRepository.findByJobAndCandidate(testJob, testCandidate);

        assertEquals(1, applications.size());
    }

    @Test
    void testFindByJobIdAndCandidateId() {
        applicationRepository.save(testApplication);

        Optional<Application> foundApplication = applicationRepository.findByJobIdAndCandidateId(
                testJob.getId(), testCandidate.getId());

        assertTrue(foundApplication.isPresent());
        assertEquals(testApplication.getId(), foundApplication.get().getId());
    }

    @Test
    void testExistsByJobIdAndCandidateId() {
        applicationRepository.save(testApplication);

        boolean exists = applicationRepository.existsByJobIdAndCandidateId(
                testJob.getId(), testCandidate.getId());
        boolean notExists = applicationRepository.existsByJobIdAndCandidateId(
                testJob.getId(), "nonexistent-id");

        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    void testCountByJobId() {
        applicationRepository.save(testApplication);

        long count = applicationRepository.countByJobId(testJob.getId());

        assertEquals(1, count);
    }

    @Test
    void testCountByCandidateId() {
        applicationRepository.save(testApplication);

        long count = applicationRepository.countByCandidateId(testCandidate.getId());

        assertEquals(1, count);
    }
}