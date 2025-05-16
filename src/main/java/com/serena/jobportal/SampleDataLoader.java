//Checking database connectivity from models and filling with sample data

/*package com.serena.jobportal;

import com.serena.jobportal.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SampleDataLoader implements CommandLineRunner {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Clear existing data (optional)
        mongoTemplate.dropCollection(User.class);
        mongoTemplate.dropCollection(Job.class);
        mongoTemplate.dropCollection(Candidate.class);
        mongoTemplate.dropCollection(Application.class);
        mongoTemplate.dropCollection(Company.class);
        mongoTemplate.dropCollection(Recruiter.class);

        // Create and save a sample User
        User user = new User("John", "Doe", new Date(), "password123hash", "CANDIDATE");
        User savedUser = mongoTemplate.save(user);
        System.out.println("Saved User: " + savedUser);

        // Create and save a sample Company
        Company.Location location = new Company.Location("New York", "NY", "USA");
        Company company = new Company("Acme Corporation", location);
        Company savedCompany = mongoTemplate.save(company);
        System.out.println("Saved Company: " + savedCompany);

        // Create and save a sample Recruiter
        Recruiter recruiter = new Recruiter(savedUser.getId(), savedCompany.getId(),
                "Experienced tech recruiter with 10+ years in the industry", "555-123-4567");
        Recruiter savedRecruiter = mongoTemplate.save(recruiter);
        System.out.println("Saved Recruiter: " + savedRecruiter);

        // Create and save a sample Job
        Job.SalaryRange salaryRange = new Job.SalaryRange(75000, 120000);
        Job job = new Job(savedRecruiter.getId(), "Senior Java Developer",
                "Join our team to develop enterprise applications using Spring Boot and MongoDB",
                salaryRange, "FULL_TIME");
        Job savedJob = mongoTemplate.save(job);
        System.out.println("Saved Job: " + savedJob);

        // Create and save a sample Candidate
        Candidate candidate = new Candidate(savedUser.getId());

        // Add skills
        candidate.getSkills().add(new Candidate.Skill("Java", "Advanced Java programming"));
        candidate.getSkills().add(new Candidate.Skill("Spring Boot", "Building REST APIs"));
        candidate.getSkills().add(new Candidate.Skill("MongoDB", "Database design and queries"));

        // Add education
        candidate.getEducations().add(new Candidate.Education("University of Technology", 4,
                "Bachelor's in Computer Science"));

        // Add experience
        candidate.getExperiences().add(new Candidate.Experience("Software Engineer", 3.5,
                "Developed enterprise applications using Java and Spring"));

        Candidate savedCandidate = mongoTemplate.save(candidate);
        System.out.println("Saved Candidate: " + savedCandidate);

        // Create and save a sample Application
        Application application = new Application(savedCandidate.getId(), savedJob.getId(), new Date());
        Application savedApplication = mongoTemplate.save(application);
        System.out.println("Saved Application: " + savedApplication);

        System.out.println("Sample data loaded successfully. Open MongoDB Compass to view the collections.");
    }
}*/
