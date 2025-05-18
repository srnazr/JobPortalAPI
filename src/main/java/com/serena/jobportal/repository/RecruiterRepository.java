package com.serena.jobportal.repository;

import com.serena.jobportal.model.Recruiter;
import com.serena.jobportal.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruiterRepository extends MongoRepository<Recruiter, String> {
    Optional<Recruiter> findByUser(User user);
    Optional<Recruiter> findByUserId(String userId);
    List<Recruiter> findByCompanyName(String companyName);
    boolean existsByUserId(String userId);
}