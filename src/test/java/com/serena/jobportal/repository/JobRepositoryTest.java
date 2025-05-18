package com.serena.jobportal.repository;

import com.serena.jobportal.model.Job;
import com.serena.jobportal.model.Recruiter;
import com.serena.jobportal.model.User;
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

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class JobRepositoryTest {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    private Recruiter testRecruiter;
    private Job testJob;

    @BeforeEach
    void setUp() {
        User testUser = User.builder()
                .firstName("Mark")
                .lastName("Wilson")
                .email("mark.wilson@example.com")
                .password("password123")
                .active(true)
                .build();
        testUser = userRepository.save(testUser);

        testRecruiter = Recruiter.builder()
                .user(testUser)
                .companyName("ABC Tech")
                .companyDescription("Software company")
                .build();
        testRecruiter = recruiterRepository.save(testRecruiter);

        testJob = Job.builder()
                .recruiter(testRecruiter)
                .title("Senior Java Developer")
                .description("We are looking for an experienced Java developer...")
                .companyName("ABC Tech")
                .location("San Francisco, CA")
                .remote(true)
                .employmentType(Job.EmploymentType.FULL_TIME)
                .experienceLevel(Job.ExperienceLevel.SENIOR)
                .skills(Arrays.asList("Java", "Spring Boot", "MongoDB"))
                .active(true)
                .postedAt(LocalDateTime.now())
                .build();
    }

    @AfterEach
    void tearDown() {
        jobRepository.deleteAll();
        recruiterRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSaveJob() {
        Job savedJob = jobRepository.save(testJob);

        assertNotNull(savedJob);
        assertNotNull(savedJob.getId());
        assertEquals(testJob.getTitle(), savedJob.getTitle());
        assertEquals(testRecruiter.getId(), savedJob.getRecruiter().getId());
    }

    @Test
    void testFindByActive() {
        jobRepository.save(testJob);

        Job inactiveJob = Job.builder()
                .recruiter(testRecruiter)
                .title("Frontend Developer")
                .description("Frontend developer position...")
                .companyName("ABC Tech")
                .location("Remote")
                .employmentType(Job.EmploymentType.FULL_TIME)
                .active(false)
                .build();
        jobRepository.save(inactiveJob);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Job> activeJobs = jobRepository.findByActive(true, pageable);
        Page<Job> inactiveJobs = jobRepository.findByActive(false, pageable);

        assertEquals(1, activeJobs.getTotalElements());
        assertEquals(1, inactiveJobs.getTotalElements());
    }

    @Test
    void testFindByRecruiter() {
        jobRepository.save(testJob);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Job> recruiterJobs = jobRepository.findByRecruiter(testRecruiter, pageable);

        assertEquals(1, recruiterJobs.getTotalElements());
        assertEquals(testJob.getTitle(), recruiterJobs.getContent().get(0).getTitle());
    }

    @Test
    void testFindByTitleContainingIgnoreCase() {
        jobRepository.save(testJob);

        Job secondJob = Job.builder()
                .recruiter(testRecruiter)
                .title("Java Backend Developer")
                .description("Backend developer role...")
                .companyName("ABC Tech")
                .location("New York, NY")
                .employmentType(Job.EmploymentType.FULL_TIME)
                .active(true)
                .build();
        jobRepository.save(secondJob);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Job> javaJobs = jobRepository.findByTitleContainingIgnoreCase("java", pageable);
        Page<Job> seniorJobs = jobRepository.findByTitleContainingIgnoreCase("senior", pageable);

        assertEquals(2, javaJobs.getTotalElements());
        assertEquals(1, seniorJobs.getTotalElements());
    }

    @Test
    void testFindBySkillsInAndActive() {
        jobRepository.save(testJob);

        Job pythonJob = Job.builder()
                .recruiter(testRecruiter)
                .title("Python Developer")
                .description("Python developer role...")
                .companyName("ABC Tech")
                .location("Remote")
                .employmentType(Job.EmploymentType.FULL_TIME)
                .skills(Arrays.asList("Python", "Django", "PostgreSQL"))
                .active(true)
                .build();
        jobRepository.save(pythonJob);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Job> javaJobs = jobRepository.findBySkillsInAndActive(Arrays.asList("Java"), true, pageable);
        Page<Job> pythonJobs = jobRepository.findBySkillsInAndActive(Arrays.asList("Python"), true, pageable);

        assertEquals(1, javaJobs.getTotalElements());
        assertEquals(1, pythonJobs.getTotalElements());
    }
}