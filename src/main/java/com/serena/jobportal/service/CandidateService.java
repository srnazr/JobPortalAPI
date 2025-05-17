package com.serena.jobportal.service;

import com.serena.jobportal.exception.ResourceNotFoundException;
import com.serena.jobportal.exception.DuplicateResourceException;
import com.serena.jobportal.exception.BadRequestException;
import com.serena.jobportal.model.Candidate;
import com.serena.jobportal.repository.CandidateRepository;
import com.serena.jobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Candidate getCandidateById(String id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", "id", id));
    }

    public Candidate getCandidateByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new BadRequestException("User ID cannot be empty");
        }

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        Candidate candidate = candidateRepository.findByUserID(userId);
        if (candidate == null) {
            throw new ResourceNotFoundException("Candidate", "userId", userId);
        }

        return candidate;
    }

    public Candidate createCandidate(Candidate candidate) {
        if (candidate.getUserID() == null || candidate.getUserID().isEmpty()) {
            throw new BadRequestException("User ID cannot be empty");
        }

        if (!userRepository.existsById(candidate.getUserID())) {
            throw new ResourceNotFoundException("User", "id", candidate.getUserID());
        }

        if (candidateRepository.existsByUserID(candidate.getUserID())) {
            throw new DuplicateResourceException("Candidate", "userId", candidate.getUserID());
        }

        return candidateRepository.save(candidate);
    }

    public Candidate updateCandidate(String id, Candidate candidateDetails) {
        Candidate candidate = getCandidateById(id);

        if (candidateDetails.getSkills() != null) {
            candidate.setSkills(candidateDetails.getSkills());
        }

        if (candidateDetails.getEducations() != null) {
            candidate.setEducations(candidateDetails.getEducations());
        }

        if (candidateDetails.getExperiences() != null) {
            candidate.setExperiences(candidateDetails.getExperiences());
        }

        return candidateRepository.save(candidate);
    }

    public Candidate addSkill(String id, Candidate.Skill skill) {
        Candidate candidate = getCandidateById(id);

        if (skill.getSkillName() == null || skill.getSkillName().isEmpty()) {
            throw new BadRequestException("Skill name cannot be empty");
        }

        candidate.getSkills().add(skill);
        return candidateRepository.save(candidate);
    }

    public Candidate addEducation(String id, Candidate.Education education) {
        Candidate candidate = getCandidateById(id);

        if (education.getInstitution() == null || education.getInstitution().isEmpty()) {
            throw new BadRequestException("Institution name cannot be empty");
        }

        if (education.getDuration() <= 0) {
            throw new BadRequestException("Education duration must be positive");
        }

        candidate.getEducations().add(education);
        return candidateRepository.save(candidate);
    }

    public Candidate addExperience(String id, Candidate.Experience experience) {
        Candidate candidate = getCandidateById(id);

        if (experience.getTitle() == null || experience.getTitle().isEmpty()) {
            throw new BadRequestException("Experience title cannot be empty");
        }

        if (experience.getDuration() <= 0) {
            throw new BadRequestException("Experience duration must be positive");
        }

        candidate.getExperiences().add(experience);
        return candidateRepository.save(candidate);
    }

    public void deleteCandidate(String id) {
        Candidate candidate = getCandidateById(id);
        candidateRepository.delete(candidate);
    }
}