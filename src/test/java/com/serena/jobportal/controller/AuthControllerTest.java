// AuthControllerTest.java
package com.serena.jobportal.controller;

import com.serena.jobportal.dto.request.LoginRequest;
import com.serena.jobportal.dto.request.RegisterRequest;
import com.serena.jobportal.model.Role;
import com.serena.jobportal.model.User;
import com.serena.jobportal.security.JwtTokenProvider;
import com.serena.jobportal.service.AuthService;
import com.serena.jobportal.service.CandidateService;
import com.serena.jobportal.service.RecruiterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CandidateService candidateService;

    @Mock
    private RecruiterService recruiterService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    private User testUser;
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup a test user
        Role role = new Role();
        role.setName("ROLE_CANDIDATE");

        testUser = User.builder()
                .id("userId")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password123")
                .roles(new ArrayList<>(Collections.singletonList(role)))
                .active(true)
                .build();

        // Setup login request
        loginRequest = new LoginRequest();
        loginRequest.setEmail("john.doe@example.com");
        loginRequest.setPassword("password123");

        // Setup register request
        registerRequest = new RegisterRequest();
        registerRequest.setFirstName("John");
        registerRequest.setLastName("Doe");
        registerRequest.setEmail("john.doe@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole("ROLE_CANDIDATE");
    }

    @Test
    void loginTest() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.createToken(any(Authentication.class))).thenReturn("testJwtToken");
        when(authService.getUserByEmail(anyString())).thenReturn(Optional.of(testUser));

        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("Login successful"));
    }

    @Test
    void registerTest() {
        when(authService.registerUser(any(User.class), anyString())).thenReturn(testUser);

        ResponseEntity<?> response = authController.registerUser(registerRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("User registered successfully"));
    }
}