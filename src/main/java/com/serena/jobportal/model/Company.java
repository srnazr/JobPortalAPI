package com.serena.jobportal.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;

@Document(collection = "companies")
@Data
public class Company {
    @Id
    private String id;

    @NotBlank(message = "Company name is required")
    @Size(min = 2, max = 100, message = "Company name must be between 2 and 100 characters")
    private String name; // Changed from "Name" to lowercase "name"

    @NotNull(message = "Company location is required")
    private Location location;

    public Company(String name, Location location) { // Updated parameter name
        this.name = name; // Updated field reference
        this.location = location;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", " + location.toString();
    }

    @Data
    public static class Location {
        @NotBlank(message = "City is required")
        @Size(min = 2, max = 50, message = "City name must be between 2 and 50 characters")
        private String city;

        @NotBlank(message = "State is required")
        @Size(min = 2, max = 50, message = "State name must be between 2 and 50 characters")
        private String state;

        @NotBlank(message = "Country is required")
        @Size(min = 2, max = 50, message = "Country name must be between 2 and 50 characters")
        private String country;

        public Location() {}

        public Location(String city, String state, String country) {
            this.city = city;
            this.state = state;
            this.country = country;
        }

        @Override
        public String toString() {
            return "Location: " + city + ", " + state + ", " + country + ".\n";
        }
    }
}