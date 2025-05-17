package com.serena.jobportal.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;

import com.serena.jobportal.validation.ValidEmploymentType;
import com.serena.jobportal.validation.ValidSalaryRange;

@Document(collection = "jobs")
@Data
@ValidSalaryRange(message = "Minimum salary cannot be greater than maximum salary")
public class Job {
    @Id
    private String id;

    @NotBlank(message = "Recruiter ID is required")
    private String recruiterID;

    @NotBlank(message = "Job title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Job description is required")
    @Size(min = 10, message = "Description must be at least 10 characters")
    private String description;

    @NotNull(message = "Salary range is required")
    private SalaryRange range;

    @NotBlank(message = "Employment type is required")
    @ValidEmploymentType
    private String employmentType;

    public Job(String recruiterID, String title, String description, SalaryRange range, String employmentType) {
        this.recruiterID = recruiterID;
        this.title = title;
        this.description = description;
        this.range = range;
        this.employmentType = employmentType;
    }

    @Override
    public String toString() {
        return "Job ID: " + id + ", Title: " + title + ", Description: " + description + ", " + range + ", Employment Type: " + employmentType + ", Created by: " + recruiterID + "\n";
    }

    @Data
    public static class SalaryRange {
        @Min(value = 0, message = "Minimum salary cannot be negative")
        private double min;

        @Min(value = 0, message = "Maximum salary cannot be negative")
        private double max;

        public SalaryRange(double min, double max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public String toString() {
            return "Salary Range: " + min + " - " + max;
        }
    }
}