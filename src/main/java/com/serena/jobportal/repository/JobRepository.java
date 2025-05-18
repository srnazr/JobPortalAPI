// JobRepository.java
package com.serena.jobportal.repository;

import com.serena.jobportal.model.Job;
import com.serena.jobportal.model.Recruiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface JobRepository extends MongoRepository<Job, String> {
    Page<Job> findByActive(boolean active, Pageable pageable);

    Page<Job> findByRecruiter(Recruiter recruiter, Pageable pageable);

    @Query("{'title': {$regex: ?0, $options: 'i'}}")
    Page<Job> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("{'skills': {$in: ?0}, 'active': true}")
    Page<Job> findBySkillsInAndActive(List<String> skills, boolean active, Pageable pageable);

    @Query("{'location': {$regex: ?0, $options: 'i'}, 'active': true}")
    Page<Job> findByLocationContainingIgnoreCaseAndActive(String location, boolean active, Pageable pageable);

    @Query("{'experienceLevel': ?0, 'active': true}")
    Page<Job> findByExperienceLevelAndActive(Job.ExperienceLevel experienceLevel, boolean active, Pageable pageable);

    @Query("{'employmentType': ?0, 'active': true}")
    Page<Job> findByEmploymentTypeAndActive(Job.EmploymentType employmentType, boolean active, Pageable pageable);

    @Query("{'postedAt': {$gte: ?0}, 'active': true}")
    Page<Job> findByPostedAtGreaterThanEqualAndActive(LocalDateTime postedAt, boolean active, Pageable pageable);

    @Query("{'$text': {'$search': ?0}}")
    Page<Job> searchJobs(String searchTerm, Pageable pageable);

    long countByRecruiterId(String id);
}