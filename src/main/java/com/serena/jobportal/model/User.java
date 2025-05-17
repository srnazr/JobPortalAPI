package com.serena.jobportal.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;
import java.util.Date;

import com.serena.jobportal.validation.ValidUserRole;
import com.serena.jobportal.validation.StrongPassword;

@Document(collection = "users")
@Data
public class User {
    @Id
    private String id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private Date dob;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @StrongPassword
    private String passwordHash;

    @NotBlank(message = "Role is required")
    @ValidUserRole
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
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
        this.creationDate = new Date(); // Set creation date to now
    }

    @Override
    public String toString() {
        return "ID: " + id + ", First name: " + firstName + ", Last name: " + lastName + ", DoB: " + dob + ", Role: " + role + ", Creation date:" + creationDate +"\n";
    }
}