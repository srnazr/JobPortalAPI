package com.serena.jobportal.util;

import com.serena.jobportal.exception.BadRequestException;
import com.serena.jobportal.model.*;
import org.springframework.stereotype.Component;

@Component
public class ModelValidator {

    /**
     * Validates a User model
     *
     * @param user The User model to validate
     * @throws BadRequestException if validation fails
     */
    public static void validateUser(User user) {
        if (user == null) {
            throw new BadRequestException("User cannot be null");
        }

        ValidationUtil.validateNotEmpty(user.getFirstName(), "First name");
        ValidationUtil.validateNotEmpty(user.getLastName(), "Last name");
        ValidationUtil.validateEmail(user.getEmail());

        if (user.getDob() == null) {
            throw new BadRequestException("Date of birth cannot be null");
        }

        ValidationUtil.validateNotEmpty(user.getPasswordHash(), "Password");
        ValidationUtil.validateUserRole(user.getRole());
    }

    /**
     * Validates a Company model
     *
     * @param company The Company model to validate
     * @throws BadRequestException if validation fails
     */
    public static void validateCompany(Company company) {
        if (company == null) {
            throw new BadRequestException("Company cannot be null");
        }

        ValidationUtil.validateNotEmpty(company.getName(), "Company name");  // Updated to use getName() instead of Name

        if (company.getLocation() == null) {
            throw new BadRequestException("Company location cannot be null");
        }

        ValidationUtil.validateNotEmpty(company.getLocation().getCity(), "City");
        ValidationUtil.validateNotEmpty(company.getLocation().getState(), "State");
        ValidationUtil.validateNotEmpty(company.getLocation().getCountry(), "Country");
    }

    /**
     * Validates a Job model
     *
     * @param job The Job model to validate
     * @throws BadRequestException if validation fails
     */
    public static void validateJob(Job job) {
        if (job == null) {
            throw new BadRequestException("Job cannot be null");
        }

        ValidationUtil.validateNotEmpty(job.getRecruiterID(), "Recruiter ID");
        ValidationUtil.validateNotEmpty(job.getTitle(), "Job title");
        ValidationUtil.validateNotEmpty(job.getDescription(), "Job description");

        if (job.getRange() == null) {
            throw new BadRequestException("Salary range cannot be null");
        }

        ValidationUtil.validateSalaryRange(job.getRange().getMin(), job.getRange().getMax());
        ValidationUtil.validateEmploymentType(job.getEmploymentType());
    }

    /**
     * Validates a Recruiter model
     *
     * @param recruiter The Recruiter model to validate
     * @throws BadRequestException if validation fails
     */
    public static void validateRecruiter(Recruiter recruiter) {
        if (recruiter == null) {
            throw new BadRequestException("Recruiter cannot be null");
        }

        ValidationUtil.validateNotEmpty(recruiter.getUserID(), "User ID");
        ValidationUtil.validateNotEmpty(recruiter.getCompanyID(), "Company ID");
        ValidationUtil.validateNotEmpty(recruiter.getBio(), "Bio");
        ValidationUtil.validatePhone(recruiter.getContactNumber());
    }

    /**
     * Validates a Candidate model
     *
     * @param candidate The Candidate model to validate
     * @throws BadRequestException if validation fails
     */
    public static void validateCandidate(Candidate candidate) {
        if (candidate == null) {
            throw new BadRequestException("Candidate cannot be null");
        }

        ValidationUtil.validateNotEmpty(candidate.getUserID(), "User ID");
    }

    /**
     * Validates a Candidate Skill
     *
     * @param skill The Skill to validate
     * @throws BadRequestException if validation fails
     */
    public static void validateSkill(Candidate.Skill skill) {
        if (skill == null) {
            throw new BadRequestException("Skill cannot be null");
        }

        ValidationUtil.validateNotEmpty(skill.getSkillName(), "Skill name");
        ValidationUtil.validateNotEmpty(skill.getSkillDesc(), "Skill description");
    }

    /**
     * Validates a Candidate Education
     *
     * @param education The Education to validate
     * @throws BadRequestException if validation fails
     */
    public static void validateEducation(Candidate.Education education) {
        if (education == null) {
            throw new BadRequestException("Education cannot be null");
        }

        ValidationUtil.validateNotEmpty(education.getInstitution(), "Institution");
        ValidationUtil.validatePositive(education.getDuration(), "Duration");
        ValidationUtil.validateNotEmpty(education.getDescription(), "Education description");
    }

    /**
     * Validates a Candidate Experience
     *
     * @param experience The Experience to validate
     * @throws BadRequestException if validation fails
     */
    public static void validateExperience(Candidate.Experience experience) {
        if (experience == null) {
            throw new BadRequestException("Experience cannot be null");
        }

        ValidationUtil.validateNotEmpty(experience.getTitle(), "Experience title");
        ValidationUtil.validatePositive(experience.getDuration(), "Duration");
        ValidationUtil.validateNotEmpty(experience.getDescription(), "Experience description");
    }

    /**
     * Validates an Application model
     *
     * @param application The Application model to validate
     * @throws BadRequestException if validation fails
     */
    public static void validateApplication(Application application) {
        if (application == null) {
            throw new BadRequestException("Application cannot be null");
        }

        ValidationUtil.validateNotEmpty(application.getCandidateID(), "Candidate ID");
        ValidationUtil.validateNotEmpty(application.getJobID(), "Job ID");

        if (application.getApplicationDate() == null) {
            throw new BadRequestException("Application date cannot be null");
        }
    }
}