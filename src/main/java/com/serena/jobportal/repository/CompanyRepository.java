package com.serena.jobportal.repository;

import com.serena.jobportal.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CompanyRepository extends MongoRepository<Company, String> {
    //Find by name
    List<Company> findByNameIgnoreCase(String name);
    // Find by location
    List<Company> findByLocationCity(String city);
    List<Company> findByLocationState(String state);
    List<Company> findByLocationCountry(String country);
    // Delete operations
    void deleteByName(String name);
}
