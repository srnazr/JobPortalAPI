package com.serena.jobportal.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;
import java.util.Date;

@Document(collection = "applications")
@Data
public class Application {
    @Id
    private String id;

    @NotBlank(message = "Candidate ID is required")
    private String candidateID;

    @NotBlank(message = "Job ID is required")
    private String jobID;

    @NotNull(message = "Application date is required")
    @PastOrPresent(message = "Application date cannot be in the future")
    private Date applicationDate;

    public Application() {
        this.applicationDate = new Date();
    }

    public Application(String candidateID, String jobID, Date applicationDate) {
        this.candidateID = candidateID;
        this.jobID = jobID;
        this.applicationDate = applicationDate != null ? applicationDate : new Date();
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Candidate ID: " + candidateID + ", Job ID: " + jobID + ", Date: " + applicationDate + "\n";
    }
}