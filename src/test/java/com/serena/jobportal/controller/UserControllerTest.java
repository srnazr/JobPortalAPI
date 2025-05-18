// UserControllerTest.java
package com.serena.jobportal.controller;

import com.serena.jobportal.dto.response.UserResponse;
import com.serena.jobportal.model.Role;
import com.serena.jobportal.model.User;
import com.serena.jobportal.security.AuthenticationFacade;
import com.serena.jobportal.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @InjectMocks
    private UserController userController;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Role role = new Role();
        role.setName("ROLE_CANDIDATE");

        testUser = User.builder()
                .id("userId")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .roles(new ArrayList<>(Arrays.asList(role)))
                .active(true)
                .build();
    }

    @Test
    void getCurrentUserTest() {
        when(authenticationFacade.getCurrentUsername()).thenReturn("john.doe@example.com");
        when(userService.getUserByEmail(anyString())).thenReturn(Optional.of(testUser));

        ResponseEntity<?> response = userController.getCurrentUser();

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("john.doe@example.com"));
    }

    @Test
    void getUserByIdTest() {
        when(userService.getUserById(anyString())).thenReturn(Optional.of(testUser));

        ResponseEntity<?> response = userController.getUserById("userId");

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("john.doe@example.com"));
    }

    @Test
    void getAllUsersTest() {
        User secondUser = User.builder()
                .id("userId2")
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .active(true)
                .build();

        when(userService.getAllUsers()).thenReturn(Arrays.asList(testUser, secondUser));

        ResponseEntity<?> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().toString().contains("john.doe@example.com"));
        assertTrue(response.getBody().toString().contains("jane.smith@example.com"));
    }
}