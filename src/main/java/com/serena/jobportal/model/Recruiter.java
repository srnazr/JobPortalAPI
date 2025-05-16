package com.serena.jobportal.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;

@Document(collection = "recruiters")
@Data
public class Recruiter {
    @Id
    private String id;
    private String userID;
    private String companyID;
    private String bio;
    private String contactNumber;

    public Recruiter(String userID, String companyID, String bio, String contactNumber) {
        this.userID = userID;
        this.companyID = companyID;
        this.bio = bio;
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", User ID: " + userID + ", Company: " + companyID + ", Biography: " + bio + ", Contact: " + contactNumber+ "\n";
    }
}
