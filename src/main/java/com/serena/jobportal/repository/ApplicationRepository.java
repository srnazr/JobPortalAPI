package com.serena.jobportal.repository;

import com.serena.jobportal.model.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ApplicationRepository extends MongoRepository<Application, String> {
    // Find by ID
    List<Application> findByCandidateID(String candidateID);
    List<Application> findByJobID(String jobID);
    // Delete operations
    void deleteByJobID(String jobID);
    long deleteByCandidateID(String candidateID);
    // Counting
    long countByJobID(String jobID);
    long countByCandidateID(String candidateID);
}
