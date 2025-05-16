package com.serena.jobportal.repository;

import com.serena.jobportal.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface JobRepository extends MongoRepository<Job, String> {
    // Find by matches
    List<Job> findByTitleIgnoreCase(String title);
    List<Job> findByRecruiterID(String recruiterID);
    List<Job> findByEmploymentType(String employmentType);
    List<Job> findByTitleContaining(String keyword);
    List<Job> findByDescriptionContaining(String keyword);
}
