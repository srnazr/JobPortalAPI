// UserRepositoryTest.java
package com.serena.jobportal.repository;

import com.serena.jobportal.model.Role;
import com.serena.jobportal.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setName("ROLE_CANDIDATE");

        testUser = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .roles(new ArrayList<>(Arrays.asList(role)))
                .active(true)
                .build();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testSaveUser() {
        User savedUser = userRepository.save(testUser);

        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals(testUser.getEmail(), savedUser.getEmail());
    }

    @Test
    void testFindByEmail() {
        userRepository.save(testUser);

        Optional<User> foundUser = userRepository.findByEmail("john.doe@example.com");

        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getFirstName(), foundUser.get().getFirstName());
    }

    @Test
    void testExistsByEmail() {
        userRepository.save(testUser);

        boolean exists = userRepository.existsByEmail("john.doe@example.com");
        boolean notExists = userRepository.existsByEmail("nonexistent@example.com");

        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    void testFindByEmailAndActive() {
        userRepository.save(testUser);

        Optional<User> activeUser = userRepository.findByEmailAndActive("john.doe@example.com", true);
        Optional<User> inactiveUser = userRepository.findByEmailAndActive("john.doe@example.com", false);

        assertTrue(activeUser.isPresent());
        assertFalse(inactiveUser.isPresent());
    }
}