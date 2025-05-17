package com.serena.jobportal.service;

import com.serena.jobportal.exception.ResourceNotFoundException;
import com.serena.jobportal.exception.BadRequestException;
import com.serena.jobportal.model.Job;
import com.serena.jobportal.repository.JobRepository;
import com.serena.jobportal.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Job getJobById(String id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));
    }

    public List<Job> getJobsByTitle(String title) {
        if (title == null || title.isEmpty()) {
            throw new BadRequestException("Job title cannot be empty");
        }

        List<Job> jobs = jobRepository.findByTitleIgnoreCase(title);
        if (jobs.isEmpty()) {
            throw new ResourceNotFoundException("Job", "title", title);
        }

        return jobs;
    }

    public List<Job> getJobsByRecruiterId(String recruiterId) {
        if (recruiterId == null || recruiterId.isEmpty()) {
            throw new BadRequestException("Recruiter ID cannot be empty");
        }

        if (!recruiterRepository.existsById(recruiterId)) {
            throw new ResourceNotFoundException("Recruiter", "id", recruiterId);
        }

        return jobRepository.findByRecruiterID(recruiterId);
    }

    public List<Job> getJobsByEmploymentType(String employmentType) {
        if (employmentType == null || employmentType.isEmpty()) {
            throw new BadRequestException("Employment type cannot be empty");
        }

        return jobRepository.findByEmploymentType(employmentType);
    }

    public List<Job> searchJobsByTitle(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            throw new BadRequestException("Search keyword cannot be empty");
        }

        return jobRepository.findByTitleContaining(keyword);
    }

    public List<Job> searchJobsByDescription(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            throw new BadRequestException("Search keyword cannot be empty");
        }

        return jobRepository.findByDescriptionContaining(keyword);
    }

    public Job createJob(Job job) {
        validateJobDetails(job);

        // Check if recruiter exists
        if (!recruiterRepository.existsById(job.getRecruiterID())) {
            throw new ResourceNotFoundException("Recruiter", "id", job.getRecruiterID());
        }

        return jobRepository.save(job);
    }

    public Job updateJob(String id, Job jobDetails) {
        Job job = getJobById(id);

        if (jobDetails.getTitle() != null && !jobDetails.getTitle().isEmpty()) {
            job.setTitle(jobDetails.getTitle());
        }

        if (jobDetails.getDescription() != null && !jobDetails.getDescription().isEmpty()) {
            job.setDescription(jobDetails.getDescription());
        }

        if (jobDetails.getRange() != null) {
            job.setRange(jobDetails.getRange());
        }

        if (jobDetails.getEmploymentType() != null && !jobDetails.getEmploymentType().isEmpty()) {
            job.setEmploymentType(jobDetails.getEmploymentType());
        }

        return jobRepository.save(job);
    }

    public void deleteJob(String id) {
        Job job = getJobById(id);
        jobRepository.delete(job);
    }

    private void validateJobDetails(Job job) {
        if (job.getTitle() == null || job.getTitle().isEmpty()) {
            throw new BadRequestException("Job title cannot be empty");
        }

        if (job.getDescription() == null || job.getDescription().isEmpty()) {
            throw new BadRequestException("Job description cannot be empty");
        }

        if (job.getRecruiterID() == null || job.getRecruiterID().isEmpty()) {
            throw new BadRequestException("Recruiter ID cannot be empty");
        }

        if (job.getRange() == null) {
            throw new BadRequestException("Salary range cannot be null");
        }

        if (job.getRange().getMin() < 0 || job.getRange().getMax() < 0) {
            throw new BadRequestException("Salary range cannot contain negative values");
        }

        if (job.getRange().getMin() > job.getRange().getMax()) {
            throw new BadRequestException("Minimum salary cannot be greater than maximum salary");
        }

        if (job.getEmploymentType() == null || job.getEmploymentType().isEmpty()) {
            throw new BadRequestException("Employment type cannot be empty");
        }
    }
}