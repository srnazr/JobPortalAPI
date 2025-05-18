package com.serena.jobportal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequest {

    @NotBlank(message = "Job ID is required")
    private String jobId;

    @Size(max = 2000, message = "Cover letter must be less than 2000 characters")
    private String coverLetter;

    private String resumeUrl;
}