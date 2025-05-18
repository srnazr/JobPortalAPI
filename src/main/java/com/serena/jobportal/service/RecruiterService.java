// RecruiterService.java
package com.serena.jobportal.service;

import com.serena.jobportal.model.Recruiter;
import com.serena.jobportal.model.User;
import com.serena.jobportal.repository.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecruiterService {

    private final RecruiterRepository recruiterRepository;
    private final UserService userService;

    @Autowired
    public RecruiterService(RecruiterRepository recruiterRepository, UserService userService) {
        this.recruiterRepository = recruiterRepository;
        this.userService = userService;
    }

    public Recruiter createRecruiter(Recruiter recruiter, String userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }

        recruiter.setUser(userOptional.get());
        return recruiterRepository.save(recruiter);
    }

    public Optional<Recruiter> getRecruiterById(String id) {
        return recruiterRepository.findById(id);
    }

    public Optional<Recruiter> getRecruiterByUserId(String userId) {
        return recruiterRepository.findByUserId(userId);
    }

    public List<Recruiter> getRecruitersByCompanyName(String companyName) {
        return recruiterRepository.findByCompanyName(companyName);
    }

    public List<Recruiter> getAllRecruiters() {
        return recruiterRepository.findAll();
    }

    public Recruiter updateRecruiter(Recruiter recruiter) {
        return recruiterRepository.save(recruiter);
    }

    public void deleteRecruiter(String id) {
        recruiterRepository.deleteById(id);
    }

    public boolean existsByUserId(String userId) {
        return recruiterRepository.existsByUserId(userId);
    }
}