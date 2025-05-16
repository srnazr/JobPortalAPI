package com.serena.jobportal.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "applications")
@Data
public class Application {
    @Id
    private String id;
    private String candidateID;
    private String jobID;
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
