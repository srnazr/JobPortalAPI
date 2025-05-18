// CandidateRepositoryTest.java
package com.serena.jobportal.repository;

import com.serena.jobportal.model.Candidate;
import com.serena.jobportal.model.Role;
import com.serena.jobportal.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class CandidateRepositoryTest {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Candidate testCandidate;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setName("ROLE_CANDIDATE");

        testUser = User.builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .password("password123")
                .roles(new ArrayList<>(Arrays.asList(role)))
                .active(true)
                .build();

        testUser = userRepository.save(testUser);

        testCandidate = Candidate.builder()
                .user(testUser)
                .resumeSummary("Experienced software developer with 5 years in Java")
                .phoneNumber("555-123-4567")
                .skills(Arrays.asList("Java", "Spring Boot", "MongoDB"))
                .build();
    }

    @AfterEach
    void tearDown() {
        candidateRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSaveCandidate() {
        Candidate savedCandidate = candidateRepository.save(testCandidate);

        assertNotNull(savedCandidate);
        assertNotNull(savedCandidate.getId());
        assertEquals(testUser.getId(), savedCandidate.getUser().getId());
    }

    @Test
    void testFindByUser() {
        candidateRepository.save(testCandidate);

        Optional<Candidate> foundCandidate = candidateRepository.findByUser(testUser);

        assertTrue(foundCandidate.isPresent());
        assertEquals(testCandidate.getResumeSummary(), foundCandidate.get().getResumeSummary());
    }

    @Test
    void testFindByUserId() {
        candidateRepository.save(testCandidate);

        Optional<Candidate> foundCandidate = candidateRepository.findByUserId(testUser.getId());

        assertTrue(foundCandidate.isPresent());
        assertEquals(testCandidate.getResumeSummary(), foundCandidate.get().getResumeSummary());
    }

    @Test
    void testExistsByUserId() {
        candidateRepository.save(testCandidate);

        boolean exists = candidateRepository.existsByUserId(testUser.getId());
        boolean notExists = candidateRepository.existsByUserId("nonexistent-id");

        assertTrue(exists);
        assertFalse(notExists);
    }
}