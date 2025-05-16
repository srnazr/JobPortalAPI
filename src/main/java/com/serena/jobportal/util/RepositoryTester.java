//test code

/*package com.serena.jobportal.util;

import com.serena.jobportal.model.*;
import com.serena.jobportal.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

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
        System.out.println("\n=== TESTING ALL REPOSITORIES ===\n");

        testUserRepository();
        testCompanyRepository();
        testRecruiterRepository();
        testJobRepository();
        testCandidateRepository();
        testApplicationRepository();

        System.out.println("\n=== ALL REPOSITORY TESTS COMPLETED ===\n");
    }

    public void testUserRepository() {
        System.out.println("Testing UserRepository...");

        // Test findAll
        List<User> users = userRepository.findAll();
        System.out.println("Found " + users.size() + " users");

        // Print first few users
        int count = 0;
        for (User user : users) {
            if (count++ < 3) {
                System.out.println("  - " + user.getFirstName() + " " +
                        user.getLastName() + " (Role: " + user.getRole() + ")");
            }
        }

        // Test findByRole
        List<User> candidates = userRepository.findByRole("CANDIDATE");
        System.out.println("Found " + candidates.size() + " candidates");

        List<User> recruiters = userRepository.findByRole("RECRUITER");
        System.out.println("Found " + recruiters.size() + " recruiters");

        System.out.println("UserRepository test completed");
    }

    public void testCompanyRepository() {
        System.out.println("\nTesting CompanyRepository...");

        // Test findAll
        List<Company> companies = companyRepository.findAll();
        System.out.println("Found " + companies.size() + " companies");

        // Print first few companies
        int count = 0;
        for (Company company : companies) {
            if (count++ < 3) {
                System.out.println("  - " + company.getName() + " (" +
                        company.getLocation().getCity() + ", " +
                        company.getLocation().getCountry() + ")");
            }
        }

        // Test findByLocationCountry
        List<Company> usCompanies = companyRepository.findByLocationCountry("USA");
        System.out.println("Found " + usCompanies.size() + " companies in USA");

        System.out.println("CompanyRepository test completed");
    }

    public void testRecruiterRepository() {
        System.out.println("\nTesting RecruiterRepository...");

        // Test findAll
        List<Recruiter> recruiters = recruiterRepository.findAll();
        System.out.println("Found " + recruiters.size() + " recruiters");

        // Test findByCompanyID if recruiters exist
        if (!recruiters.isEmpty()) {
            String companyId = recruiters.get(0).getCompanyID();
            List<Recruiter> companyRecruiters = recruiterRepository.findByCompanyID(companyId);
            System.out.println("Found " + companyRecruiters.size() +
                    " recruiters for company " + companyId);
        }

        System.out.println("RecruiterRepository test completed");
    }

    public void testJobRepository() {
        System.out.println("\nTesting JobRepository...");

        // Test findAll
        List<Job> jobs = jobRepository.findAll();
        System.out.println("Found " + jobs.size() + " jobs");

        // Print first few jobs
        int count = 0;
        for (Job job : jobs) {
            if (count++ < 3) {
                System.out.println("  - " + job.getTitle() + " (" +
                        job.getEmploymentType() + "), Salary: " +
                        job.getRange().getMin() + "-" + job.getRange().getMax());
            }
        }

        // Test findByEmploymentType
        List<Job> fullTimeJobs = jobRepository.findByEmploymentType("FULL_TIME");
        System.out.println("Found " + fullTimeJobs.size() + " FULL_TIME jobs");

        System.out.println("JobRepository test completed");
    }

    public void testCandidateRepository() {
        System.out.println("\nTesting CandidateRepository...");

        // Test findAll
        List<Candidate> candidates = candidateRepository.findAll();
        System.out.println("Found " + candidates.size() + " candidates");

        // Print details of first candidate if available
        if (!candidates.isEmpty()) {
            Candidate candidate = candidates.get(0);
            System.out.println("  - Candidate ID: " + candidate.getId());
            System.out.println("    Skills: " + candidate.getSkills().size());
            System.out.println("    Education: " + candidate.getEducations().size());
            System.out.println("    Experience: " + candidate.getExperiences().size());
        }

        System.out.println("CandidateRepository test completed");
    }

    public void testApplicationRepository() {
        System.out.println("\nTesting ApplicationRepository...");

        // Test findAll
        List<Application> applications = applicationRepository.findAll();
        System.out.println("Found " + applications.size() + " applications");

        // Test findByJobID if applications exist
        if (!applications.isEmpty()) {
            String jobId = applications.get(0).getJobID();
            List<Application> jobApps = applicationRepository.findByJobID(jobId);
            System.out.println("Found " + jobApps.size() + " applications for job " + jobId);

            long jobAppCount = applicationRepository.countByJobID(jobId);
            System.out.println("Count returned " + jobAppCount + " applications for job " + jobId);
        }

        System.out.println("ApplicationRepository test completed");
    }
}*/