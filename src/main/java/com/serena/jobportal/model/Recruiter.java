package com.serena.jobportal.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;

import com.serena.jobportal.validation.ValidPhoneNumber;

@Document(collection = "recruiters")
@Data
public class Recruiter {
    @Id
    private String id;

    @NotBlank(message = "User ID is required")
    private String userID;

    @NotBlank(message = "Company ID is required")
    private String companyID;

    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;

    @ValidPhoneNumber(message = "Contact number must be a valid phone number")
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