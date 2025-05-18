package com.serena.jobportal.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationFacadeTest {

    @InjectMocks
    private AuthenticationFacade authenticationFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAuthenticationTest() {
        // Create a mock authentication
        UserDetails userDetails = new User("john.doe@example.com", "password", new ArrayList<>());
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Set it in the security context
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);

        Authentication result = authenticationFacade.getAuthentication();

        assertNotNull(result);
        assertEquals(auth, result);
    }

    @Test
    void getCurrentUsernameFromUserDetailsTest() {
        // Create a mock authentication with UserDetails
        UserDetails userDetails = new User("john.doe@example.com", "password", new ArrayList<>());
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // Set it in the security context
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);

        String username = authenticationFacade.getCurrentUsername();

        assertEquals("john.doe@example.com", username);
    }

    @Test
    void getCurrentUsernameFromStringTest() {
        // Create a mock authentication with a String principal
        Authentication auth = new UsernamePasswordAuthenticationToken("john.doe@example.com", null, new ArrayList<>());

        // Set it in the security context
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(auth);
        SecurityContextHolder.setContext(securityContext);

        String username = authenticationFacade.getCurrentUsername();

        assertEquals("john.doe@example.com", username);
    }

    @Test
    void getCurrentUsernameNoAuthenticationTest() {
        // Clear the security context
        SecurityContextHolder.clearContext();

        String username = authenticationFacade.getCurrentUsername();

        assertNull(username);
    }
}