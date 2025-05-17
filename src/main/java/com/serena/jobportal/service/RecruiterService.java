package com.serena.jobportal.service;

import com.serena.jobportal.exception.ResourceNotFoundException;
import com.serena.jobportal.exception.DuplicateResourceException;
import com.serena.jobportal.exception.BadRequestException;
import com.serena.jobportal.model.Recruiter;
import com.serena.jobportal.repository.RecruiterRepository;
import com.serena.jobportal.repository.UserRepository;
import com.serena.jobportal.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruiterService {

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    public List<Recruiter> getAllRecruiters() {
        return recruiterRepository.findAll();
    }

    public Recruiter getRecruiterById(String id) {
        return recruiterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recruiter", "id", id));
    }

    public Recruiter getRecruiterByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new BadRequestException("User ID cannot be empty");
        }

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        Recruiter recruiter = recruiterRepository.findByUserID(userId);
        if (recruiter == null) {
            throw new ResourceNotFoundException("Recruiter", "userId", userId);
        }

        return recruiter;
    }

    public List<Recruiter> getRecruitersByCompanyId(String companyId) {
        if (companyId == null || companyId.isEmpty()) {
            throw new BadRequestException("Company ID cannot be empty");
        }

        if (!companyRepository.existsById(companyId)) {
            throw new ResourceNotFoundException("Company", "id", companyId);
        }

        return recruiterRepository.findByCompanyID(companyId);
    }

    public Recruiter createRecruiter(Recruiter recruiter) {
        if (recruiter.getUserID() == null || recruiter.getUserID().isEmpty()) {
            throw new BadRequestException("User ID cannot be empty");
        }

        if (recruiter.getCompanyID() == null || recruiter.getCompanyID().isEmpty()) {
            throw new BadRequestException("Company ID cannot be empty");
        }

        if (!userRepository.existsById(recruiter.getUserID())) {
            throw new ResourceNotFoundException("User", "id", recruiter.getUserID());
        }

        if (!companyRepository.existsById(recruiter.getCompanyID())) {
            throw new ResourceNotFoundException("Company", "id", recruiter.getCompanyID());
        }

        if (recruiterRepository.existsByUserID(recruiter.getUserID())) {
            throw new DuplicateResourceException("Recruiter", "userId", recruiter.getUserID());
        }

        return recruiterRepository.save(recruiter);
    }

    public Recruiter updateRecruiter(String id, Recruiter recruiterDetails) {
        Recruiter recruiter = getRecruiterById(id);

        if (recruiterDetails.getBio() != null) {
            recruiter.setBio(recruiterDetails.getBio());
        }

        if (recruiterDetails.getContactNumber() != null) {
            recruiter.setContactNumber(recruiterDetails.getContactNumber());
        }

        if (recruiterDetails.getCompanyID() != null && !recruiterDetails.getCompanyID().isEmpty()) {
            if (!companyRepository.existsById(recruiterDetails.getCompanyID())) {
                throw new ResourceNotFoundException("Company", "id", recruiterDetails.getCompanyID());
            }
            recruiter.setCompanyID(recruiterDetails.getCompanyID());
        }

        return recruiterRepository.save(recruiter);
    }

    public void deleteRecruiter(String id) {
        Recruiter recruiter = getRecruiterById(id);
        recruiterRepository.delete(recruiter);
    }

    public void deleteRecruiterByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new BadRequestException("User ID cannot be empty");
        }

        Recruiter recruiter = recruiterRepository.findByUserID(userId);
        if (recruiter == null) {
            throw new ResourceNotFoundException("Recruiter", "userId", userId);
        }

        recruiterRepository.deleteByUserID(userId);
    }
}