# Job Portal API Documentation

## Overview

The Job Portal API is a RESTful web service built with Spring Boot and MongoDB that facilitates job posting, job searching, and job application processes. It provides a platform for recruiters to post job openings and for candidates to search and apply for jobs.

## Table of Contents

- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Environment Setup](#environment-setup)
- [API Endpoints](#api-endpoints)
    - [Authentication](#authentication)
    - [User Management](#user-management)
    - [Candidate Management](#candidate-management)
    - [Recruiter Management](#recruiter-management)
    - [Job Management](#job-management)
    - [Application Management](#application-management)
- [Data Models](#data-models)
- [Security](#security)
- [Deployment](#deployment)

## Technology Stack

- **Java 17**: Core programming language
- **Spring Boot 3.4.5**: Framework for building the API
- **Spring Security**: Authentication and authorization
- **Spring Data MongoDB**: MongoDB integration
- **JWT (JSON Web Token)**: Secure API authentication
- **Maven**: Project management and build tool
- **MongoDB**: NoSQL database for data storage
- **Docker**: Containerization for deployment
- **JUnit**: Testing framework

## Project Structure

The application follows a standard Spring Boot project structure with the following main packages:

- `com.serena.jobportal`: Main package
    - `.config`: Configuration classes
    - `.controller`: REST controllers
    - `.dto`: Data Transfer Objects
        - `.request`: Request DTOs
        - `.response`: Response DTOs
    - `.model`: Domain models/entities
    - `.repository`: Data access repositories
    - `.security`: Security configuration
    - `.service`: Business logic services
    - `.util`: Utility classes

## Environment Setup

### Prerequisites

- Java 17+
- MongoDB
- Maven 3.9+

### Configuration

Configuration files:
- `application.properties`: Default configuration
- `application-prod.properties`: Production configuration

Key environment variables:
- `MONGODB_URI`: MongoDB connection string
- `MONGODB_DATABASE`: Database name
- `JWT_SECRET`: Secret key for JWT token signing
- `JWT_EXPIRATION`: JWT token expiration time in milliseconds
- `JWT_ISSUER`: JWT token issuer name
- `PORT`: Server port (default: 8080)

### Running Locally

1. Clone the repository
2. Configure MongoDB connection in `application.properties`
3. Run with Maven:

```bash
./mvnw spring-boot:run
```

### Running with Docker

Build and run the Docker image:

```bash
docker build -t job-portal-api .
docker run -p 8080:8080 -e MONGODB_URI=your_mongodb_uri -e JWT_SECRET=your_jwt_secret job-portal-api
```

## API Endpoints

### Authentication

| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|--------------|
| POST | `/api/auth/register` | Register a new user | None |
| POST | `/api/auth/login` | Login and get JWT token | None |

### User Management

| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|--------------|
| GET | `/api/users/me` | Get current user profile | Authenticated |
| GET | `/api/users/{id}` | Get user by ID | ADMIN |
| GET | `/api/users` | Get all users | ADMIN |

### Candidate Management

| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|--------------|
| POST | `/api/candidates` | Create candidate profile | Authenticated |
| GET | `/api/candidates/me` | Get current candidate profile | CANDIDATE |
| GET | `/api/candidates/{id}` | Get candidate by ID | Any |
| PUT | `/api/candidates/me` | Update candidate profile | CANDIDATE |

### Recruiter Management

| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|--------------|
| POST | `/api/recruiters` | Create recruiter profile | Authenticated |
| GET | `/api/recruiters/me` | Get current recruiter profile | RECRUITER |
| GET | `/api/recruiters/{id}` | Get recruiter by ID | Any |
| PUT | `/api/recruiters/me` | Update recruiter profile | RECRUITER |

### Job Management

| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|--------------|
| POST | `/api/jobs` | Create a new job | RECRUITER |
| GET | `/api/jobs/{id}` | Get job by ID | Any |
| GET | `/api/jobs` | Get all active jobs | Any |
| GET | `/api/jobs/recruiter` | Get recruiter's jobs | RECRUITER |
| POST | `/api/jobs/search` | Search for jobs | Any |
| PUT | `/api/jobs/{id}` | Update job | RECRUITER (owner) |
| PUT | `/api/jobs/{id}/activate` | Activate job | RECRUITER (owner) |
| PUT | `/api/jobs/{id}/deactivate` | Deactivate job | RECRUITER (owner) |

### Application Management

| Method | Endpoint | Description | Role Required |
|--------|----------|-------------|--------------|
| POST | `/api/applications` | Apply for a job | CANDIDATE |
| GET | `/api/applications/candidate` | Get candidate's applications | CANDIDATE |
| GET | `/api/applications/job/{jobId}` | Get job applications | RECRUITER |
| GET | `/api/applications/{id}` | Get application by ID | Authenticated |
| PUT | `/api/applications/{id}/status` | Update application status | RECRUITER |

## Data Models

### User

```json
{
  "id": "string",
  "firstName": "string",
  "lastName": "string",
  "email": "string",
  "password": "string (hashed)",
  "roles": ["string"],
  "active": "boolean",
  "createdAt": "dateTime",
  "updatedAt": "dateTime"
}
```

### Candidate

```json
{
  "id": "string",
  "user": "User object",
  "resumeSummary": "string",
  "phoneNumber": "string",
  "linkedInProfile": "string",
  "githubProfile": "string",
  "portfolioUrl": "string",
  "resumeUrl": "string",
  "skills": ["string"],
  "educations": ["Education objects"],
  "experiences": ["Experience objects"]
}
```

### Recruiter

```json
{
  "id": "string",
  "user": "User object",
  "companyName": "string",
  "companyDescription": "string",
  "companyWebsite": "string",
  "phoneNumber": "string",
  "position": "string",
  "linkedInProfile": "string"
}
```

### Job

```json
{
  "id": "string",
  "recruiter": "Recruiter object",
  "title": "string",
  "description": "string",
  "companyName": "string",
  "location": "string",
  "remote": "boolean",
  "employmentType": "FULL_TIME, PART_TIME, CONTRACT, INTERNSHIP, TEMPORARY, FREELANCE",
  "salary": "string",
  "requirements": ["string"],
  "responsibilities": ["string"],
  "skills": ["string"],
  "experienceLevel": "ENTRY, JUNIOR, MID, SENIOR, LEAD, EXECUTIVE",
  "postedAt": "dateTime",
  "deadline": "dateTime",
  "active": "boolean"
}
```

### Application

```json
{
  "id": "string",
  "job": "Job object",
  "candidate": "Candidate object",
  "coverLetter": "string",
  "resumeUrl": "string",
  "status": "APPLIED, UNDER_REVIEW, SHORTLISTED, INTERVIEW_SCHEDULED, HIRED, REJECTED",
  "appliedAt": "dateTime",
  "updatedAt": "dateTime",
  "notes": "string"
}
```

### Education

```json
{
  "institutionName": "string",
  "degree": "string",
  "fieldOfStudy": "string",
  "startDate": "date",
  "endDate": "date",
  "current": "boolean",
  "description": "string"
}
```

### Experience

```json
{
  "companyName": "string",
  "position": "string",
  "startDate": "date",
  "endDate": "date",
  "current": "boolean",
  "location": "string",
  "description": "string"
}
```

## Security

The API uses JWT (JSON Web Token) for authentication and authorization:

1. Users authenticate via the `/api/auth/login` endpoint
2. A JWT token is generated and returned
3. Subsequent requests include the JWT token in the Authorization header
4. Protected endpoints verify the token and user roles

Security features:
- Password encryption using BCrypt
- Role-based access control
- Stateless authentication
- Token expiration

## Deployment

The API can be deployed to any cloud provider that supports Docker containers. The project includes configuration for deploying to Render:

### Render Deployment (render.yaml)

```yaml
services:
  - type: web
    name: job-portal-api
    env: docker
    dockerfilePath: Dockerfile
    envVars:
      - key: MONGODB_URI
        sync: false
      - key: MONGODB_DATABASE
        value: JobPortalDB
      - key: JWT_SECRET
        sync: false
      - key: JWT_EXPIRATION
        value: 86400000
      - key: JWT_ISSUER
        value: JobPortalAPI
      - key: JAVA_OPTS
        value: "-XX:+UseContainerSupport -Xmx512m -Xms256m"
```

### Docker Configuration

The application includes a Dockerfile for containerization:

```dockerfile
FROM eclipse-temurin:17-jdk as build
WORKDIR /app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x ./mvnw
RUN ./mvnw dependency:go-offline -B
COPY src src
RUN ./mvnw package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/job-portal-api-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

## Testing

The project includes JUnit tests for:
- Controllers
- Services
- Repositories
- Security components

Run tests using Maven:

```bash
./mvnw test
```
