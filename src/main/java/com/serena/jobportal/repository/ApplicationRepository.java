// ApplicationRepository.java
package com.serena.jobportal.repository;

import com.serena.jobportal.model.Application;
import com.serena.jobportal.model.Candidate;
import com.serena.jobportal.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
    List<Application> findByJobAndStatus(Job job, Application.Status status);

    Page<Application> findByJob(Job job, Pageable pageable);

    Page<Application> findByCandidate(Candidate candidate, Pageable pageable);

    List<Application> findByJobAndCandidate(Job job, Candidate candidate);

    Optional<Application> findByJobIdAndCandidateId(String jobId, String candidateId);

    boolean existsByJobIdAndCandidateId(String jobId, String candidateId);

    Page<Application> findByStatus(Application.Status status, Pageable pageable);

    Page<Application> findByJobIdAndStatus(String jobId, Application.Status status, Pageable pageable);

    Page<Application> findByCandidateIdAndStatus(String candidateId, Application.Status status, Pageable pageable);

    Page<Application> findByAppliedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    long countByJobId(String jobId);

    long countByCandidateId(String candidateId);

    long countByJobIdAndStatus(String jobId, Application.Status status);
}