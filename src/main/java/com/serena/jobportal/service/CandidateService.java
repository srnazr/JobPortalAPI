package com.serena.jobportal.service;

import com.serena.jobportal.model.Candidate;
import com.serena.jobportal.model.User;
import com.serena.jobportal.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final UserService userService;

    @Autowired
    public CandidateService(CandidateRepository candidateRepository, UserService userService) {
        this.candidateRepository = candidateRepository;
        this.userService = userService;
    }

    public Candidate createCandidate(Candidate candidate, String userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        candidate.setUser(userOptional.get());
        return candidateRepository.save(candidate);
    }

    public Optional<Candidate> getCandidateById(String id) {
        return candidateRepository.findById(id);
    }

    public Optional<Candidate> getCandidateByUserId(String userId) {
        return candidateRepository.findByUserId(userId);
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Candidate updateCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public void deleteCandidate(String id) {
        candidateRepository.deleteById(id);
    }

    public boolean existsByUserId(String userId) {
        return candidateRepository.existsByUserId(userId);
    }
}