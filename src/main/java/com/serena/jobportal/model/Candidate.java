package com.serena.jobportal.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "candidates")
@Data
public class Candidate {
    @Id
    private String id;

    @NotBlank(message = "User ID is required")
    private String userID;

    private List<Skill> skills = new ArrayList<>();
    private List<Education> educations = new ArrayList<>();
    private List<Experience> experiences = new ArrayList<>();

    @Data
    public static class Skill {
        @NotBlank(message = "Skill name is required")
        @Size(min = 2, max = 50, message = "Skill name must be between 2 and 50 characters")
        private String skillName;

        @Size(max = 500, message = "Skill description cannot exceed 500 characters")
        private String skillDesc;

        public Skill(String skillName, String skillDesc){
            this.skillDesc=skillDesc;
            this.skillName=skillName;
        }

        @Override
        public String toString() {
            return "Skill: " + skillName + "\nDescription: " + skillDesc + "\n";
        }
    }

    @Data
    public static class Education {
        @NotBlank(message = "Institution name is required")
        @Size(min = 2, max = 100, message = "Institution name must be between 2 and 100 characters")
        private String institution;

        @Min(value = 1, message = "Duration must be at least 1 year")
        @Max(value = 10, message = "Duration cannot exceed 10 years")
        private int duration;

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        private String description;

        public Education(String institution, int duration, String description){
            this.institution=institution;
            this.description=description;
            this.duration=duration;
        }

        @Override
        public String toString() {
            return "Institution: " + institution + "\nDuration: " + duration + "\nDescription: " + description + "\n";
        }
    }

    @Data
    public static class Experience {  // Made static to match other inner classes
        @NotBlank(message = "Experience title is required")
        @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
        private String title;

        @DecimalMin(value = "0.1", message = "Duration must be at least 0.1 years")
        private double duration;

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        private String description;

        public Experience(String title, double duration, String description){
            this.title=title;
            this.duration=duration;
            this.description=description;
        }

        @Override
        public String toString() {
            return "Title: " + title + "\nDuration: " + duration + "\nDescription: " + description + "\n";
        }
    }

    public Candidate(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", User ID: " + userID + ", Skills: " + skills + ", Education: " + educations + ", Experience: " + experiences + "\n";
    }
}