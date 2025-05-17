package com.serena.jobportal.service;

import com.serena.jobportal.exception.ResourceNotFoundException;
import com.serena.jobportal.exception.DuplicateResourceException;
import com.serena.jobportal.exception.BadRequestException;
import com.serena.jobportal.model.Company;
import com.serena.jobportal.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company getCompanyById(String id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company", "id", id));
    }

    public List<Company> getCompaniesByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Company name cannot be empty");
        }

        List<Company> companies = companyRepository.findByNameIgnoreCase(name);
        if (companies.isEmpty()) {
            throw new ResourceNotFoundException("Company", "name", name);
        }

        return companies;
    }

    public List<Company> getCompaniesByCity(String city) {
        if (city == null || city.isEmpty()) {
            throw new BadRequestException("City cannot be empty");
        }

        return companyRepository.findByLocationCity(city);
    }

    public List<Company> getCompaniesByState(String state) {
        if (state == null || state.isEmpty()) {
            throw new BadRequestException("State cannot be empty");
        }

        return companyRepository.findByLocationState(state);
    }

    public List<Company> getCompaniesByCountry(String country) {
        if (country == null || country.isEmpty()) {
            throw new BadRequestException("Country cannot be empty");
        }

        return companyRepository.findByLocationCountry(country);
    }

    public Company createCompany(Company company) {
        if (company.getName() == null || company.getName().isEmpty()) { // Updated to use getName()
            throw new BadRequestException("Company name cannot be empty");
        }

        if (company.getLocation() == null) {
            throw new BadRequestException("Company location cannot be null");
        }

        List<Company> existingCompanies = companyRepository.findByNameIgnoreCase(company.getName()); // Updated to use getName()
        if (!existingCompanies.isEmpty()) {
            throw new DuplicateResourceException("Company", "name", company.getName()); // Updated to use getName()
        }

        return companyRepository.save(company);
    }

    public Company updateCompany(String id, Company companyDetails) {
        Company company = getCompanyById(id);

        if (companyDetails.getName() != null && !companyDetails.getName().isEmpty()) { // Updated to use getName()
            company.setName(companyDetails.getName()); // Updated to use getName() and setName()
        }

        if (companyDetails.getLocation() != null) {
            company.setLocation(companyDetails.getLocation());
        }

        return companyRepository.save(company);
    }

    public void deleteCompany(String id) {
        Company company = getCompanyById(id);
        companyRepository.delete(company);
    }

    public void deleteCompanyByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new BadRequestException("Company name cannot be empty");
        }

        List<Company> companies = companyRepository.findByNameIgnoreCase(name);
        if (companies.isEmpty()) {
            throw new ResourceNotFoundException("Company", "name", name);
        }

        companyRepository.deleteByName(name);
    }
}