package com.serena.jobportal.dto.response;

import com.serena.jobportal.model.Education;
import com.serena.jobportal.model.Experience;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateResponse {

    private String id;

    private String userId;

    private String firstName;

    private String lastName;

    private String email;

    private String resumeSummary;

    private String phoneNumber;

    private String linkedInProfile;

    private String githubProfile;

    private String portfolioUrl;

    private String resumeUrl;

    private String profilePictureUrl;

    private List<String> skills;

    private List<Education> educations;

    private List<Experience> experiences;

    private long applicationsCount;
}