// AuthController.java
package com.serena.jobportal.controller;

import com.serena.jobportal.dto.request.LoginRequest;
import com.serena.jobportal.dto.request.RegisterRequest;
import com.serena.jobportal.dto.response.JwtResponse;
import com.serena.jobportal.dto.response.UserResponse;
import com.serena.jobportal.model.Role;
import com.serena.jobportal.model.User;
import com.serena.jobportal.security.JwtTokenProvider;
import com.serena.jobportal.service.AuthService;
import com.serena.jobportal.service.CandidateService;
import com.serena.jobportal.service.RecruiterService;
import com.serena.jobportal.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final CandidateService candidateService;
    private final RecruiterService recruiterService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Generate JWT token
        String jwt = tokenProvider.createToken(authentication);

        // Get user details
        User user = authService.getUserByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());

        String profileId = null;
        String profileType = null;

        // Check if user is a candidate or recruiter
        if (roles.contains("ROLE_CANDIDATE")) {
            var candidateOptional = candidateService.getCandidateByUserId(user.getId());
            if (candidateOptional.isPresent()) {
                profileId = candidateOptional.get().getId();
                profileType = "CANDIDATE";
            }
        } else if (roles.contains("ROLE_RECRUITER")) {
            var recruiterOptional = recruiterService.getRecruiterByUserId(user.getId());
            if (recruiterOptional.isPresent()) {
                profileId = recruiterOptional.get().getId();
                profileType = "RECRUITER";
            }
        }

        JwtResponse jwtResponse = JwtResponse.builder()
                .accessToken(jwt)
                .tokenType("Bearer")
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .roles(roles)
                .profileId(profileId)
                .profileType(profileType)
                .build();

        return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", jwtResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        User newUser = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(registerRequest.getPassword())
                .build();

        User registeredUser = authService.registerUser(newUser, registerRequest.getRole());

        UserResponse userResponse = UserResponse.builder()
                .id(registeredUser.getId())
                .firstName(registeredUser.getFirstName())
                .lastName(registeredUser.getLastName())
                .email(registeredUser.getEmail())
                .roles(registeredUser.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toList()))
                .active(registeredUser.isActive())
                .createdAt(registeredUser.getCreatedAt())
                .build();

        return ResponseEntity.ok(new ApiResponse<>(true, "User registered successfully", userResponse));
    }
}