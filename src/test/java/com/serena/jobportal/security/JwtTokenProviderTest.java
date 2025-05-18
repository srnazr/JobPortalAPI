// JwtTokenProviderTest.java
package com.serena.jobportal.security;

import com.serena.jobportal.config.JwtConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JwtTokenProviderTest {

    @Mock
    private JwtConfig jwtConfig;

    @InjectMocks
    private JwtTokenProvider tokenProvider;

    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(jwtConfig.getSecret()).thenReturn("testSecretKeyWhichIsAtLeast256BitsLongForTestingPurposes");
        when(jwtConfig.getExpirationMs()).thenReturn(3600000L); // 1 hour
        when(jwtConfig.getIssuer()).thenReturn("testIssuer");

        tokenProvider.init(); // Initialize the secret key

        authentication = new UsernamePasswordAuthenticationToken(
                "test@example.com",
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    void createTokenTest() {
        String token = tokenProvider.createToken(authentication);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void validateTokenTest() {
        String token = tokenProvider.createToken(authentication);

        boolean isValid = tokenProvider.validateToken(token);

        assertTrue(isValid);
    }

    @Test
    void getUsernameFromTokenTest() {
        String token = tokenProvider.createToken(authentication);

        String username = tokenProvider.getUsernameFromToken(token);

        assertEquals("test@example.com", username);
    }

    @Test
    void getAuthenticationTest() {
        String token = tokenProvider.createToken(authentication);

        Authentication resultAuth = tokenProvider.getAuthentication(token);

        assertNotNull(resultAuth);
        assertEquals("test@example.com", resultAuth.getName());
        assertTrue(resultAuth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }
}