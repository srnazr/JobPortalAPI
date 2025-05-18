// JobSearchRequest.java
package com.serena.jobportal.dto.request;

import com.serena.jobportal.model.Job;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobSearchRequest {

    private String keyword;

    private String location;

    private Boolean remote;

    private Job.EmploymentType employmentType;

    private Job.ExperienceLevel experienceLevel;

    private List<String> skills;

    private String companyName;

    private PaginationRequest pagination;

    private SortRequest sort;
}