// JwtResponse.java
package com.serena.jobportal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    private String accessToken;

    private String tokenType;

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private List<String> roles;

    private String profileId;

    private String profileType;
}