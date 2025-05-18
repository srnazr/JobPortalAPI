// RecruiterResponse.java
package com.serena.jobportal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecruiterResponse {

    private String id;

    private String userId;

    private String firstName;

    private String lastName;

    private String email;

    private String companyName;

    private String companyDescription;

    private String companyWebsite;

    private String phoneNumber;

    private String position;

    private String linkedInProfile;

    private String profilePictureUrl;

    private long jobsCount;
}