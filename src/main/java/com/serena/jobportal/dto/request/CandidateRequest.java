// CandidateRequest.java
package com.serena.jobportal.dto.request;

import com.serena.jobportal.model.Education;
import com.serena.jobportal.model.Experience;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateRequest {

    @Size(max = 1000, message = "Resume summary must be less than 1000 characters")
    private String resumeSummary;

    @Size(max = 20, message = "Phone number must be less than 20 characters")
    private String phoneNumber;

    @Size(max = 255, message = "LinkedIn profile must be less than 255 characters")
    private String linkedInProfile;

    @Size(max = 255, message = "Github profile must be less than 255 characters")
    private String githubProfile;

    @Size(max = 255, message = "Portfolio URL must be less than 255 characters")
    private String portfolioUrl;

    private String resumeUrl;

    private String profilePictureUrl;

    private List<String> skills;

    @Valid
    private List<Education> educations;

    @Valid
    private List<Experience> experiences;
}