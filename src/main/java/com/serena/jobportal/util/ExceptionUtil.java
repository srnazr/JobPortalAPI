package com.serena.jobportal.util;

import com.serena.jobportal.exception.ResourceNotFoundException;
import com.serena.jobportal.exception.DuplicateResourceException;
import com.serena.jobportal.model.*;
import org.springframework.stereotype.Component;

@Component
public class ExceptionUtil {

    public static void validateUserExists(String userId, boolean exists, String operation) {
        if (!exists && operation.equals("FIND")) {
            throw new ResourceNotFoundException("User", "id", userId);
        } else if (exists && operation.equals("CREATE")) {
            throw new DuplicateResourceException("User", "id", userId);
        }
    }

    public static void validateEmailExists(String email, boolean exists, String operation) {
        if (!exists && operation.equals("FIND")) {
            throw new ResourceNotFoundException("User", "email", email);
        } else if (exists && operation.equals("CREATE")) {
            throw new DuplicateResourceException("User", "email", email);
        }
    }

    public static void validateCompanyExists(String companyId, boolean exists, String operation) {
        if (!exists && operation.equals("FIND")) {
            throw new ResourceNotFoundException("Company", "id", companyId);
        } else if (exists && operation.equals("CREATE")) {
            throw new DuplicateResourceException("Company", "id", companyId);
        }
    }

    public static void validateJobExists(String jobId, boolean exists, String operation) {
        if (!exists && operation.equals("FIND")) {
            throw new ResourceNotFoundException("Job", "id", jobId);
        } else if (exists && operation.equals("CREATE")) {
            throw new DuplicateResourceException("Job", "id", jobId);
        }
    }

    public static void validateCandidateExists(String candidateId, boolean exists, String operation) {
        if (!exists && operation.equals("FIND")) {
            throw new ResourceNotFoundException("Candidate", "id", candidateId);
        } else if (exists && operation.equals("CREATE")) {
            throw new DuplicateResourceException("Candidate", "id", candidateId);
        }
    }

    public static void validateRecruiterExists(String recruiterId, boolean exists, String operation) {
        if (!exists && operation.equals("FIND")) {
            throw new ResourceNotFoundException("Recruiter", "id", recruiterId);
        } else if (exists && operation.equals("CREATE")) {
            throw new DuplicateResourceException("Recruiter", "id", recruiterId);
        }
    }

    public static void validateApplicationExists(String applicationId, boolean exists, String operation) {
        if (!exists && operation.equals("FIND")) {
            throw new ResourceNotFoundException("Application", "id", applicationId);
        } else if (exists && operation.equals("CREATE")) {
            throw new DuplicateResourceException("Application", "id", applicationId);
        }
    }
}