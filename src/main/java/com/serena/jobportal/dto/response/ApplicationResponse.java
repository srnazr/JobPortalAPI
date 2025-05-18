package com.serena.jobportal.dto.response;

import com.serena.jobportal.model.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationResponse {

    private String id;

    private String jobId;

    private String jobTitle;

    private String companyName;

    private String candidateId;

    private String candidateName;

    private String coverLetter;

    private String resumeUrl;

    private Application.Status status;

    private LocalDateTime appliedAt;

    private LocalDateTime updatedAt;

    private String notes;
}