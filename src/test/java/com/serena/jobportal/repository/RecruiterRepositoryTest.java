// RecruiterRepositoryTest.java
package com.serena.jobportal.repository;

import com.serena.jobportal.model.Recruiter;
import com.serena.jobportal.model.Role;
import com.serena.jobportal.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class RecruiterRepositoryTest {

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Recruiter testRecruiter;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setName("ROLE_RECRUITER");

        testUser = User.builder()
                .firstName("Robert")
                .lastName("Johnson")
                .email("robert.johnson@example.com")
                .password("password123")
                .roles(new ArrayList<>(Arrays.asList(role)))
                .active(true)
                .build();

        testUser = userRepository.save(testUser);

        testRecruiter = Recruiter.builder()
                .user(testUser)
                .companyName("TechCorp")
                .companyDescription("Leading technology company")
                .companyWebsite("https://techcorp.example.com")
                .position("Senior Technical Recruiter")
                .phoneNumber("555-987-6543")
                .build();
    }

    @AfterEach
    void tearDown() {
        recruiterRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testSaveRecruiter() {
        Recruiter savedRecruiter = recruiterRepository.save(testRecruiter);

        assertNotNull(savedRecruiter);
        assertNotNull(savedRecruiter.getId());
        assertEquals(testUser.getId(), savedRecruiter.getUser().getId());
    }

    @Test
    void testFindByUser() {
        recruiterRepository.save(testRecruiter);

        Optional<Recruiter> foundRecruiter = recruiterRepository.findByUser(testUser);

        assertTrue(foundRecruiter.isPresent());
        assertEquals(testRecruiter.getCompanyName(), foundRecruiter.get().getCompanyName());
    }

    @Test
    void testFindByUserId() {
        recruiterRepository.save(testRecruiter);

        Optional<Recruiter> foundRecruiter = recruiterRepository.findByUserId(testUser.getId());

        assertTrue(foundRecruiter.isPresent());
        assertEquals(testRecruiter.getCompanyName(), foundRecruiter.get().getCompanyName());
    }

    @Test
    void testFindByCompanyName() {
        recruiterRepository.save(testRecruiter);

        User secondUser = User.builder()
                .firstName("Sarah")
                .lastName("Williams")
                .email("sarah.williams@example.com")
                .password("password456")
                .active(true)
                .build();
        secondUser = userRepository.save(secondUser);

        Recruiter secondRecruiter = Recruiter.builder()
                .user(secondUser)
                .companyName("TechCorp")
                .position("Technical Recruiter")
                .build();
        recruiterRepository.save(secondRecruiter);

        List<Recruiter> recruiters = recruiterRepository.findByCompanyName("TechCorp");

        assertEquals(2, recruiters.size());
    }

    @Test
    void testExistsByUserId() {
        recruiterRepository.save(testRecruiter);

        boolean exists = recruiterRepository.existsByUserId(testUser.getId());
        boolean notExists = recruiterRepository.existsByUserId("nonexistent-id");

        assertTrue(exists);
        assertFalse(notExists);
    }
}