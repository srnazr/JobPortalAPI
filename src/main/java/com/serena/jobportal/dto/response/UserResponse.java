package com.serena.jobportal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private List<String> roles;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String profileId;

    private String profileType;
}