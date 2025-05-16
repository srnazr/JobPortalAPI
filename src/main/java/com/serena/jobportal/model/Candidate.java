package com.serena.jobportal.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "candidates")
@Data
public class Candidate {
    @Id
    private String id;
    private String userID;
    private List<Skill> skills = new ArrayList<>();
    private List<Education> educations = new ArrayList<>();
    private List<Experience> experiences = new ArrayList<>();

    @Data
    public static class Skill {  // Made static
        private String skillName;
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
    public static class Education {  // Made static
        private String institution;
        private int duration;
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
    public static class Experience {
        private String title;
        private double duration;
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