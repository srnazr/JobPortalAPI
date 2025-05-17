package com.serena.jobportal.service;

import com.serena.jobportal.exception.ResourceNotFoundException;
import com.serena.jobportal.exception.DuplicateResourceException;
import com.serena.jobportal.exception.BadRequestException;
import com.serena.jobportal.model.Application;
import com.serena.jobportal.repository.ApplicationRepository;
import com.serena.jobportal.repository.CandidateRepository;
import com.serena.jobportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Date;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private JobRepository jobRepository;

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Application getApplicationById(String id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application", "id", id));
    }

    public List<Application> getApplicationsByCandidateId(String candidateId) {
        if (candidateId == null || candidateId.isEmpty()) {
            throw new BadRequestException("Candidate ID cannot be empty");
        }

        // Updated to use userID instead of id for candidate check
        if (!candidateRepository.existsByUserID(candidateId)) {
            throw new ResourceNotFoundException("Candidate", "userId", candidateId);
        }

        return applicationRepository.findByCandidateID(candidateId);
    }

    public List<Application> getApplicationsByJobId(String jobId) {
        if (jobId == null || jobId.isEmpty()) {
            throw new BadRequestException("Job ID cannot be empty");
        }

        if (!jobRepository.existsById(jobId)) {
            throw new ResourceNotFoundException("Job", "id", jobId);
        }

        return applicationRepository.findByJobID(jobId);
    }

    public long countApplicationsByJobId(String jobId) {
        if (jobId == null || jobId.isEmpty()) {
            throw new BadRequestException("Job ID cannot be empty");
        }

        if (!jobRepository.existsById(jobId)) {
            throw new ResourceNotFoundException("Job", "id", jobId);
        }

        return applicationRepository.countByJobID(jobId);
    }

    public long countApplicationsByCandidateId(String candidateId) {
        if (candidateId == null || candidateId.isEmpty()) {
            throw new BadRequestException("Candidate ID cannot be empty");
        }

        // Updated to use userID instead of id for candidate check
        if (!candidateRepository.existsByUserID(candidateId)) {
            throw new ResourceNotFoundException("Candidate", "userId", candidateId);
        }

        return applicationRepository.countByCandidateID(candidateId);
    }

    public Application createApplication(Application application) {
        if (application.getCandidateID() == null || application.getCandidateID().isEmpty()) {
            throw new BadRequestException("Candidate ID cannot be empty");
        }

        if (application.getJobID() == null || application.getJobID().isEmpty()) {
            throw new BadRequestException("Job ID cannot be empty");
        }

        // Updated to use userID instead of id for candidate check
        if (!candidateRepository.existsByUserID(application.getCandidateID())) {
            throw new ResourceNotFoundException("Candidate", "userId", application.getCandidateID());
        }

        if (!jobRepository.existsById(application.getJobID())) {
            throw new ResourceNotFoundException("Job", "id", application.getJobID());
        }

        // Check if application already exists
        List<Application> existingApplications = applicationRepository.findByCandidateID(application.getCandidateID());
        boolean alreadyApplied = existingApplications.stream()
                .anyMatch(app -> app.getJobID().equals(application.getJobID()));

        if (alreadyApplied) {
            throw new DuplicateResourceException("Application", "candidateId and jobId",
                    application.getCandidateID() + " - " + application.getJobID());
        }

        // Set application date to now if not provided
        if (application.getApplicationDate() == null) {
            application.setApplicationDate(new Date());
        }

        return applicationRepository.save(application);
    }

    public void deleteApplication(String id) {
        Application application = getApplicationById(id);
        applicationRepository.delete(application);
    }

    public void deleteApplicationsByJobId(String jobId) {
        if (jobId == null || jobId.isEmpty()) {
            throw new BadRequestException("Job ID cannot be empty");
        }

        if (!jobRepository.existsById(jobId)) {
            throw new ResourceNotFoundException("Job", "id", jobId);
        }

        applicationRepository.deleteByJobID(jobId);
    }

    public long deleteApplicationsByCandidateId(String candidateId) {
        if (candidateId == null || candidateId.isEmpty()) {
            throw new BadRequestException("Candidate ID cannot be empty");
        }

        // Updated to use userID instead of id for candidate check
        if (!candidateRepository.existsByUserID(candidateId)) {
            throw new ResourceNotFoundException("Candidate", "userId", candidateId);
        }

        return applicationRepository.deleteByCandidateID(candidateId);
    }
}