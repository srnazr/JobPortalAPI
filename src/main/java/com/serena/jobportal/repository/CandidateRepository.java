// CandidateRepository.java
package com.serena.jobportal.repository;

import com.serena.jobportal.model.Candidate;
import com.serena.jobportal.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends MongoRepository<Candidate, String> {
    Optional<Candidate> findByUser(User user);
    Optional<Candidate> findByUserId(String userId);
    boolean existsByUserId(String userId);
}