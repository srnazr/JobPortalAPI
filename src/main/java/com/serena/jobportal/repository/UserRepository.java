package com.serena.jobportal.repository;

import com.serena.jobportal.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    //Find By Name
    User findByFirstNameAndLastName(String firstName, String lastName);
    //Find By Role
    List<User> findByRole(String role);
    long countByRole(String role);
    //Existence Check
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
    boolean existsByEmail(String email);
}
