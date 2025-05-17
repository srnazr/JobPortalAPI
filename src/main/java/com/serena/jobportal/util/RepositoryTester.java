package com.serena.jobportal.util;

import com.serena.jobportal.exception.ResourceNotFoundException;
import com.serena.jobportal.model.*;
import com.serena.jobportal.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class RepositoryTester {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    public void testAllRepositories() {
        try {
            testUserRepository();
            testCompanyRepository();
            testRecruiterRepository();
            testJobRepository();
            testCandidateRepository();
            testApplicationRepository();

            System.out.println("All repository tests completed successfully!");
        } catch (Exception e) {
            System.err.println("Error in repository tests: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void testUserRepository() {
        System.out.println("Testing UserRepository...");

        try {
            // Get all users
            List<User> users = userRepository.findAll();
            System.out.println("Found " + users.size() + " users");

            if (!users.isEmpty()) {
                // Get first user
                User firstUser = users.get(0);
                String userId = firstUser.getId();

                // Find by ID
                Optional<User> foundUser = userRepository.findById(userId);
                if (foundUser.isPresent()) {
                    System.out.println("Found user by ID: " + foundUser.get());
                } else {
                    throw new ResourceNotFoundException("User", "id", userId);
                }

                // Find by name
                User userByName = userRepository.findByFirstNameAndLastName(
                        firstUser.getFirstName(), firstUser.getLastName());
                System.out.println("Found user by name: " + userByName);

                // Find by role
                List<User> usersByRole = userRepository.findByRole(firstUser.getRole());
                System.out.println("Found " + usersByRole.size() + " users with role: " + firstUser.getRole());

                // Count by role
                long countByRole = userRepository.countByRole(firstUser.getRole());
                System.out.println("Count by role " + firstUser.getRole() + ": " + countByRole);

                // Check existence
                boolean exists = userRepository.existsByFirstNameAndLastName(
                        firstUser.getFirstName(), firstUser.getLastName());
                System.out.println("User exists by name: " + exists);

                boolean emailExists = userRepository.existsByEmail(firstUser.getEmail());
                System.out.println("User exists by email: " + emailExists);
            }

            System.out.println("UserRepository tests passed!");
        } catch (Exception e) {
            System.err.println("Error in UserRepository tests: " + e.getMessage());
            throw e;
        }
    }

    private void testCompanyRepository() {
        System.out.println("Testing CompanyRepository...");

        try {
            // Get all companies
            List<Company> companies = companyRepository.findAll();
            System.out.println("Found " + companies.size() + " companies");

            if (!companies.isEmpty()) {
                // Get first company
                Company firstCompany = companies.get(0);
                String companyId = firstCompany.getId();

                // Find by ID
                Optional<Company> foundCompany = companyRepository.findById(companyId);
                if (foundCompany.isPresent()) {
                    System.out.println("Found company by ID: " + foundCompany.get());
                } else {
                    throw new ResourceNotFoundException("Company", "id", companyId);
                }

                // Find by name - Updated to use getName() instead of Name
                List<Company> companiesByName = companyRepository.findByNameIgnoreCase(firstCompany.getName());
                System.out.println("Found " + companiesByName.size() + " companies with name: " + firstCompany.getName());

                // Find by location
                List<Company> companiesByCity = companyRepository.findByLocationCity(firstCompany.getLocation().getCity());
                System.out.println("Found " + companiesByCity.size() + " companies in city: " + firstCompany.getLocation().getCity());

                List<Company> companiesByState = companyRepository.findByLocationState(firstCompany.getLocation().getState());
                System.out.println("Found " + companiesByState.size() + " companies in state: " + firstCompany.getLocation().getState());

                List<Company> companiesByCountry = companyRepository.findByLocationCountry(firstCompany.getLocation().getCountry());
                System.out.println("Found " + companiesByCountry.size() + " companies in country: " + firstCompany.getLocation().getCountry());
            }

            System.out.println("CompanyRepository tests passed!");
        } catch (Exception e) {
            System.err.println("Error in CompanyRepository tests: " + e.getMessage());
            throw e;
        }
    }

    private void testRecruiterRepository() {
        System.out.println("Testing RecruiterRepository...");

        try {
            // Get all recruiters
            List<Recruiter> recruiters = recruiterRepository.findAll();
            System.out.println("Found " + recruiters.size() + " recruiters");

            if (!recruiters.isEmpty()) {
                // Get first recruiter
                Recruiter firstRecruiter = recruiters.get(0);
                String recruiterId = firstRecruiter.getId();

                // Find by ID
                Optional<Recruiter> foundRecruiter = recruiterRepository.findById(recruiterId);
                if (foundRecruiter.isPresent()) {
                    System.out.println("Found recruiter by ID: " + foundRecruiter.get());
                } else {
                    throw new ResourceNotFoundException("Recruiter", "id", recruiterId);
                }

                // Find by user ID
                Recruiter recruiterByUserId = recruiterRepository.findByUserID(firstRecruiter.getUserID());
                System.out.println("Found recruiter by user ID: " + recruiterByUserId);

                // Find by company ID
                List<Recruiter> recruitersByCompanyId = recruiterRepository.findByCompanyID(firstRecruiter.getCompanyID());
                System.out.println("Found " + recruitersByCompanyId.size() + " recruiters with company ID: " + firstRecruiter.getCompanyID());

                // Check existence
                boolean exists = recruiterRepository.existsByUserID(firstRecruiter.getUserID());
                System.out.println("Recruiter exists by user ID: " + exists);
            }

            System.out.println("RecruiterRepository tests passed!");
        } catch (Exception e) {
            System.err.println("Error in RecruiterRepository tests: " + e.getMessage());
            throw e;
        }
    }

    private void testJobRepository() {
        System.out.println("Testing JobRepository...");

        try {
            // Get all jobs
            List<Job> jobs = jobRepository.findAll();
            System.out.println("Found " + jobs.size() + " jobs");

            if (!jobs.isEmpty()) {
                // Get first job
                Job firstJob = jobs.get(0);
                String jobId = firstJob.getId();

                // Find by ID
                Optional<Job> foundJob = jobRepository.findById(jobId);
                if (foundJob.isPresent()) {
                    System.out.println("Found job by ID: " + foundJob.get());
                } else {
                    throw new ResourceNotFoundException("Job", "id", jobId);
                }

                // Find by title
                List<Job> jobsByTitle = jobRepository.findByTitleIgnoreCase(firstJob.getTitle());
                System.out.println("Found " + jobsByTitle.size() + " jobs with title: " + firstJob.getTitle());

                // Find by recruiter ID
                List<Job> jobsByRecruiterId = jobRepository.findByRecruiterID(firstJob.getRecruiterID());
                System.out.println("Found " + jobsByRecruiterId.size() + " jobs with recruiter ID: " + firstJob.getRecruiterID());

                // Find by employment type
                List<Job> jobsByEmploymentType = jobRepository.findByEmploymentType(firstJob.getEmploymentType());
                System.out.println("Found " + jobsByEmploymentType.size() + " jobs with employment type: " + firstJob.getEmploymentType());

                // Find by title containing
                List<Job> jobsByTitleContaining = jobRepository.findByTitleContaining(firstJob.getTitle().substring(0, 3));
                System.out.println("Found " + jobsByTitleContaining.size() + " jobs with title containing: " + firstJob.getTitle().substring(0, 3));

                // Find by description containing
                List<Job> jobsByDescriptionContaining = jobRepository.findByDescriptionContaining(firstJob.getDescription().substring(0, 5));
                System.out.println("Found " + jobsByDescriptionContaining.size() + " jobs with description containing: " + firstJob.getDescription().substring(0, 5));
            }

            System.out.println("JobRepository tests passed!");
        } catch (Exception e) {
            System.err.println("Error in JobRepository tests: " + e.getMessage());
            throw e;
        }
    }

    private void testCandidateRepository() {
        System.out.println("Testing CandidateRepository...");

        try {
            // Get all candidates
            List<Candidate> candidates = candidateRepository.findAll();
            System.out.println("Found " + candidates.size() + " candidates");

            if (!candidates.isEmpty()) {
                // Get first candidate
                Candidate firstCandidate = candidates.get(0);
                String candidateId = firstCandidate.getId();

                // Find by ID
                Optional<Candidate> foundCandidate = candidateRepository.findById(candidateId);
                if (foundCandidate.isPresent()) {
                    System.out.println("Found candidate by ID: " + foundCandidate.get());
                } else {
                    throw new ResourceNotFoundException("Candidate", "id", candidateId);
                }

                // Find by user ID
                Candidate candidateByUserId = candidateRepository.findByUserID(firstCandidate.getUserID());
                System.out.println("Found candidate by user ID: " + candidateByUserId);

                // Check existence
                boolean exists = candidateRepository.existsByUserID(firstCandidate.getUserID());
                System.out.println("Candidate exists by user ID: " + exists);
            }

            System.out.println("CandidateRepository tests passed!");
        } catch (Exception e) {
            System.err.println("Error in CandidateRepository tests: " + e.getMessage());
            throw e;
        }
    }

    private void testApplicationRepository() {
        System.out.println("Testing ApplicationRepository...");

        try {
            // Get all applications
            List<Application> applications = applicationRepository.findAll();
            System.out.println("Found " + applications.size() + " applications");

            if (!applications.isEmpty()) {
                // Get first application
                Application firstApplication = applications.get(0);
                String applicationId = firstApplication.getId();

                // Find by ID
                Optional<Application> foundApplication = applicationRepository.findById(applicationId);
                if (foundApplication.isPresent()) {
                    System.out.println("Found application by ID: " + foundApplication.get());
                } else {
                    throw new ResourceNotFoundException("Application", "id", applicationId);
                }

                // Find by candidate ID
                List<Application> applicationsByCandidateId = applicationRepository.findByCandidateID(firstApplication.getCandidateID());
                System.out.println("Found " + applicationsByCandidateId.size() + " applications with candidate ID: " + firstApplication.getCandidateID());

                // Find by job ID
                List<Application> applicationsByJobId = applicationRepository.findByJobID(firstApplication.getJobID());
                System.out.println("Found " + applicationsByJobId.size() + " applications with job ID: " + firstApplication.getJobID());

                // Count by job ID
                long countByJobId = applicationRepository.countByJobID(firstApplication.getJobID());
                System.out.println("Count by job ID " + firstApplication.getJobID() + ": " + countByJobId);

                // Count by candidate ID
                long countByCandidateId = applicationRepository.countByCandidateID(firstApplication.getCandidateID());
                System.out.println("Count by candidate ID " + firstApplication.getCandidateID() + ": " + countByCandidateId);
            }

            System.out.println("ApplicationRepository tests passed!");
        } catch (Exception e) {
            System.err.println("Error in ApplicationRepository tests: " + e.getMessage());
            throw e;
        }
    }
}