package com.serena.jobportal.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "roles")
public class Role {

    @Id
    private String id;

    private String name;

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_RECRUITER = "ROLE_RECRUITER";
    public static final String ROLE_CANDIDATE = "ROLE_CANDIDATE";
}