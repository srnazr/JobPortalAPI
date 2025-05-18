package com.serena.jobportal.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "jobs")
public class Job {

    @Id
    private String id;

    @DBRef
    private Recruiter recruiter;

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    @TextIndexed
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 50, max = 5000, message = "Description must be between 50 and 5000 characters")
    @TextIndexed
    private String description;

    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String companyName;

    @NotBlank(message = "Location is required")
    @Size(min = 2, max = 100, message = "Location must be between 2 and 100 characters")
    private String location;

    private boolean remote;

    @NotNull(message = "Employment type is required")
    private EmploymentType employmentType;

    private String salary;

    @Builder.Default
    private List<String> requirements = new ArrayList<>();

    @Builder.Default
    private List<String> responsibilities = new ArrayList<>();

    @Builder.Default
    private List<String> skills = new ArrayList<>();

    private ExperienceLevel experienceLevel;

    @Builder.Default
    private LocalDateTime postedAt = LocalDateTime.now();

    private LocalDateTime deadline;

    private boolean active;

    public enum EmploymentType {
        FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP, TEMPORARY, FREELANCE
    }

    public enum ExperienceLevel {
        ENTRY, JUNIOR, MID, SENIOR, LEAD, EXECUTIVE
    }
}