package com.serena.jobportal.model;

import java.time.LocalDateTime;

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
@Document(collection = "applications")
public class Application {

    @Id
    private String id;

    @DBRef
    private Job job;

    @DBRef
    private Candidate candidate;

    @Size(max = 2000, message = "Cover letter must be less than 2000 characters")
    private String coverLetter;

    private String resumeUrl;

    private Status status;

    @Builder.Default
    private LocalDateTime appliedAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @Size(max = 1000, message = "Notes must be less than 1000 characters")
    private String notes;

    public enum Status {
        APPLIED, UNDER_REVIEW, SHORTLISTED, INTERVIEW_SCHEDULED, HIRED, REJECTED
    }
}