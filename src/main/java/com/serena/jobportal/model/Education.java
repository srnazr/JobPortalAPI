// Education.java
package com.serena.jobportal.model;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education {

    @NotBlank(message = "Institution name is required")
    @Size(min = 2, max = 100, message = "Institution name must be between 2 and 100 characters")
    private String institutionName;

    @NotBlank(message = "Degree is required")
    @Size(min = 2, max = 100, message = "Degree must be between 2 and 100 characters")
    private String degree;

    @Size(max = 100, message = "Field of study must be less than 100 characters")
    private String fieldOfStudy;

    @NotNull(message = "Start date is required")
    @PastOrPresent(message = "Start date must be in the past or present")
    private LocalDate startDate;

    private LocalDate endDate;

    private boolean current;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;
}