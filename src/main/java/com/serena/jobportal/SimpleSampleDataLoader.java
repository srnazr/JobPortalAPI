//fill database with data

/*package com.serena.jobportal;

import com.serena.jobportal.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Order(1)
public class SimpleSampleDataLoader implements CommandLineRunner {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Clear existing data
        mongoTemplate.dropCollection(User.class);
        mongoTemplate.dropCollection(Company.class);
        mongoTemplate.dropCollection(Recruiter.class);
        mongoTemplate.dropCollection(Job.class);
        mongoTemplate.dropCollection(Candidate.class);
        mongoTemplate.dropCollection(Application.class);

        System.out.println("=== Creating Simple Sample Data ===");

        // Create sample users
        List<User> users = createUsers();

        // Create sample companies
        List<Company> companies = createCompanies();

        // Create sample recruiters
        List<Recruiter> recruiters = createRecruiters(users, companies);

        // Create sample jobs
        List<Job> jobs = createJobs(recruiters);

        // Create sample candidates
        List<Candidate> candidates = createCandidates(users);

        // Create sample applications
        List<Application> applications = createApplications(candidates, jobs);

        System.out.println("=== Simple Sample Data Created ===");
        System.out.println("Created " + users.size() + " users");
        System.out.println("Created " + companies.size() + " companies");
        System.out.println("Created " + recruiters.size() + " recruiters");
        System.out.println("Created " + jobs.size() + " jobs");
        System.out.println("Created " + candidates.size() + " candidates");
        System.out.println("Created " + applications.size() + " applications");
    }

    private List<User> createUsers() {
        List<User> users = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Create 3 different users (1 candidate, 1 recruiter, 1 admin)
            // Note: Using the exact constructor parameters from your User model
            User candidate = new User(
                    "John",
                    "Doe",
                    "john.doe@example.com",
                    sdf.parse("1990-01-01"),
                    "password123",
                    "CANDIDATE"
            );
            users.add(mongoTemplate.save(candidate));

            User recruiter = new User(
                    "Jane",
                    "Smith",
                    "jane.smith@example.com",
                    sdf.parse("1985-05-15"),
                    "recruiter456",
                    "RECRUITER"
            );
            users.add(mongoTemplate.save(recruiter));

            User admin = new User(
                    "Admin",
                    "User",
                    "admin@example.com",
                    sdf.parse("1980-10-20"),
                    "admin789",
                    "ADMIN"
            );
            users.add(mongoTemplate.save(admin));

        } catch (Exception e) {
            System.err.println("Error creating users: " + e.getMessage());
        }

        return users;
    }

    private List<Company> createCompanies() {
        List<Company> companies = new ArrayList<>();

        // Create 3 companies
        Company company1 = new Company(
                "Tech Innovators",
                new Company.Location("San Francisco", "California", "USA")
        );
        companies.add(mongoTemplate.save(company1));

        Company company2 = new Company(
                "Global Systems",
                new Company.Location("New York", "New York", "USA")
        );
        companies.add(mongoTemplate.save(company2));

        Company company3 = new Company(
                "Data Solutions",
                new Company.Location("London", "England", "UK")
        );
        companies.add(mongoTemplate.save(company3));

        return companies;
    }

    private List<Recruiter> createRecruiters(List<User> users, List<Company> companies) {
        List<Recruiter> recruiters = new ArrayList<>();

        // Find recruiter user
        User recruiterUser = users.stream()
                .filter(user -> "RECRUITER".equals(user.getRole()))
                .findFirst()
                .orElse(null);

        if (recruiterUser != null && !companies.isEmpty()) {
            // Create a recruiter linked to the recruiter user and first company
            Recruiter recruiter = new Recruiter(
                    recruiterUser.getId(),
                    companies.get(0).getId(),
                    "Experienced tech recruiter with 10+ years in the industry",
                    "555-123-4567"
            );
            recruiters.add(mongoTemplate.save(recruiter));

            // Create two more dummy recruiters for testing
            Recruiter recruiter2 = new Recruiter(
                    "dummy-user-id-1",
                    companies.get(1).getId(),
                    "Senior recruiter for Global Systems",
                    "555-987-6543"
            );
            recruiters.add(mongoTemplate.save(recruiter2));

            Recruiter recruiter3 = new Recruiter(
                    "dummy-user-id-2",
                    companies.get(2).getId(),
                    "International talent specialist",
                    "555-456-7890"
            );
            recruiters.add(mongoTemplate.save(recruiter3));
        }

        return recruiters;
    }

    private List<Job> createJobs(List<Recruiter> recruiters) {
        List<Job> jobs = new ArrayList<>();

        if (!recruiters.isEmpty()) {
            // Create 3 jobs
            Job job1 = new Job(
                    recruiters.get(0).getId(),
                    "Software Engineer",
                    "We are looking for an experienced Software Engineer to join our growing team.",
                    new Job.SalaryRange(80000, 120000),
                    "FULL_TIME"
            );
            jobs.add(mongoTemplate.save(job1));

            Job job2 = new Job(
                    recruiters.get(0).getId(),
                    "Data Scientist",
                    "Join our team as a Data Scientist and help build the next generation of our analytics platform.",
                    new Job.SalaryRange(90000, 130000),
                    "FULL_TIME"
            );
            jobs.add(mongoTemplate.save(job2));

            Job job3 = new Job(
                    recruiters.get(0).getId(),
                    "Frontend Developer",
                    "Exciting opportunity for a Frontend Developer to make an impact in a fast-paced startup environment.",
                    new Job.SalaryRange(75000, 110000),
                    "REMOTE"
            );
            jobs.add(mongoTemplate.save(job3));
        }

        return jobs;
    }

    private List<Candidate> createCandidates(List<User> users) {
        List<Candidate> candidates = new ArrayList<>();

        // Find candidate user
        User candidateUser = users.stream()
                .filter(user -> "CANDIDATE".equals(user.getRole()))
                .findFirst()
                .orElse(null);

        if (candidateUser != null) {
            // Create a candidate linked to the candidate user
            Candidate candidate = new Candidate(candidateUser.getId());

            // Add skills
            candidate.getSkills().add(new Candidate.Skill("Java", "Expert in Spring Boot and microservices"));
            candidate.getSkills().add(new Candidate.Skill("Python", "Data analysis and machine learning"));

            // Add education
            candidate.getEducations().add(new Candidate.Education("Stanford University", 4, "BS in Computer Science"));

            // Add experience
            candidate.getExperiences().add(new Candidate.Experience(
                    "Software Engineer",
                    3.5,
                    "Developed and maintained backend services using Java and Spring Boot"
            ));

            candidates.add(mongoTemplate.save(candidate));

            // Create two more dummy candidates for testing
            Candidate candidate2 = new Candidate("dummy-user-id-3");
            candidate2.getSkills().add(new Candidate.Skill("JavaScript", "Frontend development with React"));
            candidate2.getEducations().add(new Candidate.Education("MIT", 4, "BS in Computer Science"));
            candidate2.getExperiences().add(new Candidate.Experience(
                    "Frontend Developer",
                    2.0,
                    "Built responsive web applications using React and Redux"
            ));
            candidates.add(mongoTemplate.save(candidate2));

            Candidate candidate3 = new Candidate("dummy-user-id-4");
            candidate3.getSkills().add(new Candidate.Skill("DevOps", "CI/CD pipeline automation"));
            candidate3.getEducations().add(new Candidate.Education("UC Berkeley", 4, "BS in Computer Engineering"));
            candidate3.getExperiences().add(new Candidate.Experience(
                    "DevOps Engineer",
                    4.0,
                    "Designed and implemented CI/CD pipelines for microservices architecture"
            ));
            candidates.add(mongoTemplate.save(candidate3));
        }

        return candidates;
    }

    private List<Application> createApplications(List<Candidate> candidates, List<Job> jobs) {
        List<Application> applications = new ArrayList<>();

        if (!candidates.isEmpty() && jobs.size() >= 3) {
            // Create 3 applications
            // First candidate applies to first job
            Application app1 = new Application(
                    candidates.get(0).getId(),
                    jobs.get(0).getId(),
                    new Date() // Current date
            );
            applications.add(mongoTemplate.save(app1));

            // First candidate applies to second job
            Application app2 = new Application(
                    candidates.get(0).getId(),
                    jobs.get(1).getId(),
                    new Date() // Current date
            );
            applications.add(mongoTemplate.save(app2));

            // Second candidate applies to third job
            if (candidates.size() >= 2) {
                Application app3 = new Application(
                        candidates.get(1).getId(),
                        jobs.get(2).getId(),
                        new Date() // Current date
                );
                applications.add(mongoTemplate.save(app3));
            }
        }

        return applications;
    }
}*/