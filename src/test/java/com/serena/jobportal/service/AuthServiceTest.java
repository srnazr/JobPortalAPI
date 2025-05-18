// AuthServiceTest.java
package com.serena.jobportal.service;

import com.serena.jobportal.model.Role;
import com.serena.jobportal.model.User;
import com.serena.jobportal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = User.builder()
                .id("userId")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .build();
    }

    @Test
    void registerUserTest() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = authService.registerUser(testUser, "ROLE_CANDIDATE");

        assertNotNull(result);
        assertEquals("userId", result.getId());
        assertEquals("john.doe@example.com", result.getEmail());
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(testUser);
    }

    @Test
    void registerUserEmailExistsTest() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                authService.registerUser(testUser, "ROLE_CANDIDATE"));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticateTest() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        Authentication result = authService.authenticate("john.doe@example.com", "password123");

        assertNotNull(result);
        assertEquals(authentication, result);
    }

    @Test
    void getUserByEmailTest() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));

        Optional<User> result = authService.getUserByEmail("john.doe@example.com");

        assertTrue(result.isPresent());
        assertEquals("john.doe@example.com", result.get().getEmail());
    }

    @Test
    void changePasswordSuccessTest() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        boolean result = authService.changePassword("john.doe@example.com", "password123", "newPassword");

        assertTrue(result);
        verify(userRepository).save(testUser);
    }

    @Test
    void changePasswordWrongCurrentPasswordTest() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        boolean result = authService.changePassword("john.doe@example.com", "wrongPassword", "newPassword");

        assertFalse(result);
        verify(userRepository, never()).save(any(User.class));
    }
}