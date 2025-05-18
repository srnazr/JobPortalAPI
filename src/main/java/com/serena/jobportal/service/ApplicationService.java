package com.serena.jobportal.service;

import com.serena.jobportal.model.Application;
import com.serena.jobportal.model.Candidate;
import com.serena.jobportal.model.Job;
import com.serena.jobportal.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobService jobService;
    private final CandidateService candidateService;

    @Autowired
    public ApplicationService(
            ApplicationRepository applicationRepository,
            JobService jobService,
            CandidateService candidateService) {
        this.applicationRepository = applicationRepository;
        this.jobService = jobService;
        this.candidateService = candidateService;
    }

    public Application createApplication(Application application, String jobId, String candidateId) {
        if (applicationRepository.existsByJobIdAndCandidateId(jobId, candidateId)) {
            throw new IllegalArgumentException("You have already applied for this job");
        }

        Optional<Job> jobOptional = jobService.getJobById(jobId);
        if (!jobOptional.isPresent()) {
            throw new IllegalArgumentException("Job not found with ID: " + jobId);
        }

        Optional<Candidate> candidateOptional = candidateService.getCandidateById(candidateId);
        if (!candidateOptional.isPresent()) {
            throw new IllegalArgumentException("Candidate not found with ID: " + candidateId);
        }

        application.setJob(jobOptional.get());
        application.setCandidate(candidateOptional.get());
        application.setStatus(Application.Status.APPLIED);
        application.setAppliedAt(LocalDateTime.now());

        return applicationRepository.save(application);
    }

    public Optional<Application> getApplicationById(String id) {
        return applicationRepository.findById(id);
    }

    public Page<Application> getApplicationsByJob(String jobId, Pageable pageable) {
        Optional<Job> jobOptional = jobService.getJobById(jobId);
        if (!jobOptional.isPresent()) {
            throw new IllegalArgumentException("Job not found with ID: " + jobId);
        }

        return applicationRepository.findByJob(jobOptional.get(), pageable);
    }

    public Page<Application> getApplicationsByCandidate(String candidateId, Pageable pageable) {
        Optional<Candidate> candidateOptional = candidateService.getCandidateById(candidateId);
        if (!candidateOptional.isPresent()) {
            throw new IllegalArgumentException("Candidate not found with ID: " + candidateId);
        }

        return applicationRepository.findByCandidate(candidateOptional.get(), pageable);
    }

    public Page<Application> getApplicationsByJobAndStatus(String jobId, Application.Status status, Pageable pageable) {
        return applicationRepository.findByJobIdAndStatus(jobId, status, pageable);
    }

    public Page<Application> getApplicationsByCandidateAndStatus(String candidateId, Application.Status status, Pageable pageable) {
        return applicationRepository.findByCandidateIdAndStatus(candidateId, status, pageable);
    }

    public Application updateApplicationStatus(String id, Application.Status status) {
        Optional<Application> applicationOptional = applicationRepository.findById(id);
        if (!applicationOptional.isPresent()) {
            throw new IllegalArgumentException("Application not found with ID: " + id);
        }

        Application application = applicationOptional.get();
        application.setStatus(status);
        application.setUpdatedAt(LocalDateTime.now());

        return applicationRepository.save(application);
    }

    public Application updateApplication(Application application) {
        application.setUpdatedAt(LocalDateTime.now());
        return applicationRepository.save(application);
    }

    public void deleteApplication(String id) {
        applicationRepository.deleteById(id);
    }

    public long countApplicationsByJob(String jobId) {
        return applicationRepository.countByJobId(jobId);
    }

    public long countApplicationsByCandidate(String candidateId) {
        return applicationRepository.countByCandidateId(candidateId);
    }
}