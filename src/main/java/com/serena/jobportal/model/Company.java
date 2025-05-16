package com.serena.jobportal.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "companies")
@Data
public class Company {
    @Id
    private String id;
    private String Name;
    private Location location;

    public Company(String Name, Location location) {
        this.Name = Name;
        this.location = location;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + Name + ", " + location.toString();
    }

    @Data
    public static class Location {
        private String city;
        private String state;
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
