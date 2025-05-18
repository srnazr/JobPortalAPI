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
public class RecruiterRequest {

    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String companyName;

    @Size(max = 500, message = "Company description must be less than 500 characters")
    private String companyDescription;

    @Size(max = 100, message = "Company website must be less than 100 characters")
    private String companyWebsite;

    @Size(max = 20, message = "Phone number must be less than 20 characters")
    private String phoneNumber;

    @Size(max = 100, message = "Position at company must be less than 100 characters")
    private String position;

    @Size(max = 255, message = "LinkedIn profile must be less than 255 characters")
    private String linkedInProfile;

    private String profilePictureUrl;
}