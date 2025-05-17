package com.serena.jobportal;

import com.serena.jobportal.model.*;
import com.serena.jobportal.repository.*;
import com.serena.jobportal.service.*;
import com.serena.jobportal.util.ValidationUtil;
import com.serena.jobportal.util.ModelValidator;
import com.serena.jobportal.exception.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.serena.jobportal.repository")
public class JobPortalApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobPortalApiApplication.class, args);
	}

	/**
	 * Initialize sample data and test all components
	 */
	@Bean
	public CommandLineRunner testApplication(
			UserRepository userRepository,
			CompanyRepository companyRepository,
			RecruiterRepository recruiterRepository,
			CandidateRepository candidateRepository,
			JobRepository jobRepository,
			ApplicationRepository applicationRepository,
			UserService userService,
			CompanyService companyService,
			RecruiterService recruiterService,
			CandidateService candidateService,
			JobService jobService,
			ApplicationService applicationService) {

		return args -> {
			System.out.println("\n========== TESTING JOBPORTAL APPLICATION ==========\n");

			// Clear existing data
			System.out.println("Clearing existing data...");
			applicationRepository.deleteAll();
			jobRepository.deleteAll();
			recruiterRepository.deleteAll();
			candidateRepository.deleteAll();
			companyRepository.deleteAll();
			userRepository.deleteAll();

			// Test data creation
			System.out.println("\n----- CREATING TEST DATA -----\n");

			// Create users
			System.out.println("Creating users...");
			User adminUser = new User(
					"Admin", "User", "admin@jobportal.com",
					new Date(), "Admin123!", "ADMIN");

			User candidateUser = new User(
					"John", "Doe", "john@example.com",
					new Date(), "Pass123!", "CANDIDATE");

			User recruiterUser = new User(
					"Jane", "Smith", "jane@example.com",
					new Date(), "Pass123!", "RECRUITER");

			List<User> users = Arrays.asList(adminUser, candidateUser, recruiterUser);
			userRepository.saveAll(users);
			System.out.println("Created " + users.size() + " users");

			// Test user retrieval
			System.out.println("\nRetrieving users...");
			List<User> retrievedUsers = userRepository.findAll();
			System.out.println("Found " + retrievedUsers.size() + " users");

			// Create company
			System.out.println("\nCreating company...");
			Company company = new Company(
					"Tech Company",
					new Company.Location("New York", "NY", "USA"));

			companyRepository.save(company);
			System.out.println("Created company: " + company.getName());

			// Test company service
			System.out.println("\nTesting company service...");
			List<Company> retrievedCompanies = companyService.getAllCompanies();
			System.out.println("Found " + retrievedCompanies.size() + " companies");

			// Create recruiter
			System.out.println("\nCreating recruiter...");
			String recruiterId = retrievedUsers.stream()
					.filter(u -> "RECRUITER".equals(u.getRole()))
					.findFirst()
					.map(User::getId)
					.orElse(null);

			Recruiter recruiter = new Recruiter(
					recruiterId,
					company.getId(),
					"Experienced recruiter",
					"+1234567890");

			recruiterRepository.save(recruiter);
			System.out.println("Created recruiter for user " + recruiterId);

			// Create candidate
			System.out.println("\nCreating candidate...");
			String candidateId = retrievedUsers.stream()
					.filter(u -> "CANDIDATE".equals(u.getRole()))
					.findFirst()
					.map(User::getId)
					.orElse(null);

			Candidate candidate = new Candidate(candidateId);

			// Add skills, education, and experience
			candidate.getSkills().add(new Candidate.Skill("Java", "Expert in Java development"));
			candidate.getEducations().add(new Candidate.Education("University", 4, "Computer Science"));
			candidate.getExperiences().add(new Candidate.Experience("Developer", 3.5, "Software development"));

			candidateRepository.save(candidate);
			System.out.println("Created candidate for user " + candidateId);

			// Create job
			System.out.println("\nCreating job...");
			Job job = new Job(
					recruiter.getId(),
					"Software Developer",
					"Develop software applications",
					new Job.SalaryRange(80000, 120000),
					"FULL_TIME");

			jobRepository.save(job);
			System.out.println("Created job: " + job.getTitle());

			// Create application
			System.out.println("\nCreating application...");
			Application application = new Application(
					candidate.getId(),
					job.getId(),
					new Date());

			applicationRepository.save(application);
			System.out.println("Created application");

			// Test validation
			System.out.println("\n----- TESTING VALIDATION -----\n");

			try {
				System.out.println("Testing email validation...");
				ValidationUtil.validateEmail("valid@example.com");
				System.out.println("Valid email passed validation");

				ValidationUtil.validateEmail("invalid-email");
				System.out.println("ERROR: Invalid email passed validation");
			} catch (BadRequestException e) {
				System.out.println("Validation correctly caught invalid email: " + e.getMessage());
			}

			try {
				System.out.println("\nTesting model validation...");
				User validUser = new User("Valid", "User", "valid@example.com", new Date(), "Password123!", "ADMIN");
				ModelValidator.validateUser(validUser);
				System.out.println("Valid user passed validation");

				User invalidUser = new User();
				ModelValidator.validateUser(invalidUser);
				System.out.println("ERROR: Invalid user passed validation");
			} catch (BadRequestException e) {
				System.out.println("Validation correctly caught invalid user: " + e.getMessage());
			}

			// Test company name field (testing the Name -> name fix)
			System.out.println("\n----- TESTING COMPANY NAME FIELD -----\n");

			Company testCompany = new Company("Test Company Name", new Company.Location("City", "State", "Country"));
			companyRepository.save(testCompany);

			List<Company> foundCompanies = companyRepository.findByNameIgnoreCase("test company name");
			if (!foundCompanies.isEmpty()) {
				System.out.println("Successfully found company by name (case insensitive): " +
						foundCompanies.get(0).getName());
			} else {
				System.out.println("ERROR: Could not find company by name");
			}

			// Test exception handling
			System.out.println("\n----- TESTING EXCEPTION HANDLING -----\n");

			try {
				userService.getUserById("non-existent-id");
				System.out.println("ERROR: No exception thrown for non-existent user");
			} catch (ResourceNotFoundException e) {
				System.out.println("Correctly threw ResourceNotFoundException: " + e.getMessage());
			}

			// Print summary
			System.out.println("\n========== TEST SUMMARY ==========\n");
			System.out.println("Users: " + userRepository.count());
			System.out.println("Companies: " + companyRepository.count());
			System.out.println("Recruiters: " + recruiterRepository.count());
			System.out.println("Candidates: " + candidateRepository.count());
			System.out.println("Jobs: " + jobRepository.count());
			System.out.println("Applications: " + applicationRepository.count());
			System.out.println("\n======================================\n");
		};
	}
}