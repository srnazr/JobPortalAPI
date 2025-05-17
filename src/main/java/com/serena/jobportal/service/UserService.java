package com.serena.jobportal.service;

import com.serena.jobportal.exception.ResourceNotFoundException;
import com.serena.jobportal.exception.DuplicateResourceException;
import com.serena.jobportal.exception.BadRequestException;
import com.serena.jobportal.model.User;
import com.serena.jobportal.repository.UserRepository;
// Removed unused import: import com.serena.jobportal.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    public User createUser(User user) {
        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("User", "email", user.getEmail());
        }

        // Check if name already exists
        if (userRepository.existsByFirstNameAndLastName(user.getFirstName(), user.getLastName())) {
            throw new DuplicateResourceException("User", "name", user.getFirstName() + " " + user.getLastName());
        }

        return userRepository.save(user);
    }

    public User updateUser(String id, User userDetails) {
        User user = getUserById(id);

        // Check if updated email already exists for another user
        if (!user.getEmail().equals(userDetails.getEmail()) &&
                userRepository.existsByEmail(userDetails.getEmail())) {
            throw new DuplicateResourceException("User", "email", userDetails.getEmail());
        }

        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setDob(userDetails.getDob());
        user.setRole(userDetails.getRole());

        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    public List<User> getUsersByRole(String role) {
        if (role == null || role.isEmpty()) {
            throw new BadRequestException("Role cannot be empty");
        }

        return userRepository.findByRole(role);
    }

    public long countUsersByRole(String role) {
        if (role == null || role.isEmpty()) {
            throw new BadRequestException("Role cannot be empty");
        }

        return userRepository.countByRole(role);
    }
}