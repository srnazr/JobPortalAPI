package com.serena.jobportal.repository;

import com.serena.jobportal.model.Recruiter;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface RecruiterRepository extends MongoRepository<Recruiter, String> {
    // Find by ID
    Recruiter findByUserID(String userID);
    List<Recruiter> findByCompanyID(String companyID);
    // Existence checks
    boolean existsByUserID(String userID);
    // Delete operations
    void deleteByUserID(String userID);
}
