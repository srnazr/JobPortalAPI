package com.serena.jobportal.repository;

import com.serena.jobportal.model.Candidate;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CandidateRepository extends MongoRepository<Candidate, String> {
    // Find by ID
    Candidate findByUserID(String userID);
    boolean existsByUserID(String userID);
}
