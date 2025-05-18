package com.serena.jobportal.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void doFilterInternalWithValidTokenTest() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer validToken");
        when(tokenProvider.validateToken(anyString())).thenReturn(true);
        when(tokenProvider.getAuthentication(anyString())).thenReturn(authentication);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(tokenProvider).validateToken("validToken");
        verify(tokenProvider).getAuthentication("validToken");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternalWithInvalidTokenTest() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");
        when(tokenProvider.validateToken(anyString())).thenReturn(false);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(tokenProvider).validateToken("invalidToken");
        verify(tokenProvider, never()).getAuthentication(anyString());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternalWithNoTokenTest() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(tokenProvider, never()).validateToken(anyString());
        verify(tokenProvider, never()).getAuthentication(anyString());
        verify(filterChain).doFilter(request, response);
    }
}