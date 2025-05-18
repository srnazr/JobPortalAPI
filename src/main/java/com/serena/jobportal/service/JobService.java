package com.serena.jobportal.service;

import com.serena.jobportal.model.Job;
import com.serena.jobportal.model.Recruiter;
import com.serena.jobportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final RecruiterService recruiterService;

    @Autowired
    public JobService(JobRepository jobRepository, RecruiterService recruiterService) {
        this.jobRepository = jobRepository;
        this.recruiterService = recruiterService;
    }

    public Job createJob(Job job, String recruiterId) {
        Optional<Recruiter> recruiterOptional = recruiterService.getRecruiterById(recruiterId);
        if (!recruiterOptional.isPresent()) {
            throw new IllegalArgumentException("Recruiter not found with ID: " + recruiterId);
        }

        job.setRecruiter(recruiterOptional.get());
        job.setPostedAt(LocalDateTime.now());
        job.setActive(true);

        return jobRepository.save(job);
    }

    public Optional<Job> getJobById(String id) {
        return jobRepository.findById(id);
    }

    public Page<Job> getActiveJobs(Pageable pageable) {
        return jobRepository.findByActive(true, pageable);
    }

    public Page<Job> getJobsByRecruiter(Recruiter recruiter, Pageable pageable) {
        return jobRepository.findByRecruiter(recruiter, pageable);
    }

    public Page<Job> searchJobsByTitle(String title, Pageable pageable) {
        return jobRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    public Page<Job> searchJobsBySkills(List<String> skills, Pageable pageable) {
        return jobRepository.findBySkillsInAndActive(skills, true, pageable);
    }

    public Page<Job> searchJobs(String searchTerm, Pageable pageable) {
        return jobRepository.searchJobs(searchTerm, pageable);
    }

    public Job updateJob(Job job) {
        return jobRepository.save(job);
    }

    public Job activateJob(String id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (!jobOptional.isPresent()) {
            throw new IllegalArgumentException("Job not found with ID: " + id);
        }

        Job job = jobOptional.get();
        job.setActive(true);
        return jobRepository.save(job);
    }

    public Job deactivateJob(String id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (!jobOptional.isPresent()) {
            throw new IllegalArgumentException("Job not found with ID: " + id);
        }

        Job job = jobOptional.get();
        job.setActive(false);
        return jobRepository.save(job);
    }

    public void deleteJob(String id) {
        jobRepository.deleteById(id);
    }
}