// JobResponse.java
package com.serena.jobportal.dto.response;

import com.serena.jobportal.model.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobResponse {

    private String id;

    private String recruiterId;

    private String recruiterName;

    private String title;

    private String description;

    private String companyName;

    private String location;

    private boolean remote;

    private Job.EmploymentType employmentType;

    private String salary;

    private List<String> requirements;

    private List<String> responsibilities;

    private List<String> skills;

    private Job.ExperienceLevel experienceLevel;

    private LocalDateTime postedAt;

    private LocalDateTime deadline;

    private boolean active;

    private long applicationsCount;
}