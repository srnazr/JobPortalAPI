package com.serena.jobportal.controller;

import com.serena.jobportal.model.Company;
import com.serena.jobportal.service.CompanyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable String id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Company>> getCompaniesByName(@PathVariable String name) {
        return ResponseEntity.ok(companyService.getCompaniesByName(name));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Company>> getCompaniesByCity(@PathVariable String city) {
        return ResponseEntity.ok(companyService.getCompaniesByCity(city));
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<Company>> getCompaniesByState(@PathVariable String state) {
        return ResponseEntity.ok(companyService.getCompaniesByState(state));
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<Company>> getCompaniesByCountry(@PathVariable String country) {
        return ResponseEntity.ok(companyService.getCompaniesByCountry(country));
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        Company createdCompany = companyService.createCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompany);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable String id, @Valid @RequestBody Company companyDetails) {
        return ResponseEntity.ok(companyService.updateCompany(id, companyDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable String id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/name/{name}")
    public ResponseEntity<Void> deleteCompanyByName(@PathVariable String name) {
        companyService.deleteCompanyByName(name);
        return ResponseEntity.noContent().build();
    }
}