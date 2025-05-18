// Candidate.java
package com.serena.jobportal.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "candidates")
public class Candidate {

    @Id
    private String id;

    @DBRef
    private User user;

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

    @Builder.Default
    private List<String> skills = new ArrayList<>();

    @Builder.Default
    private List<Education> educations = new ArrayList<>();

    @Builder.Default
    private List<Experience> experiences = new ArrayList<>();
}