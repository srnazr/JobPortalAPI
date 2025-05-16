package com.serena.jobportal.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "users")
@Data
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private Date dob;
    private String email;
    private String passwordHash;
    private String role;
    private Date creationDate;

    //Set the date to current date in the default constructor
    public User() {
        this.creationDate = new Date();
    }

    public User(String firstName, String lastName, String email, Date dob, String passwordHash, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.email=email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.creationDate = new Date(); // Set creation date to now
    }

    @Override
    public String toString() {
        return "ID: " + id + ", First name: " + firstName + ", Last name: " + lastName + ", DoB: " + dob + ", Role: " + role + ", Creation date:" + creationDate +"\n";
    }
}
