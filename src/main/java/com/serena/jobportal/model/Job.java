package com.serena.jobportal.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "jobs")
@Data
public class Job {
    @Id
    private String id;
    private String recruiterID;
    private String title;
    private String description;
    private SalaryRange range;
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
        private double min;
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
