Exception Handling in Job Portal API
This document explains the exception handling structure implemented in the Job Portal API.

Exception Hierarchy
Base Exception Class
JobPortalException - Base exception class that extends RuntimeException
Specific Exception Types
ResourceNotFoundException - Thrown when a requested resource is not found (HTTP 404)
DuplicateResourceException - Thrown when attempting to create a duplicate resource (HTTP 409)
BadRequestException - Thrown when request data is invalid (HTTP 400)
UnauthorizedException - Thrown for authentication failures (HTTP 401)
ForbiddenException - Thrown for permission issues (HTTP 403)
Global Exception Handler
The GlobalExceptionHandler class centrally handles all exceptions using Spring's @ControllerAdvice. It provides:

Consistent error responses
HTTP status code mapping
Detailed error messages for debugging
Error Response Format
All API errors follow a consistent format:

json
{
"timestamp": "2025-05-17T10:30:45.123Z",
"message": "Descriptive error message",
"details": "Request details"
}
For validation errors, additional field-level details are provided:

json
{
"timestamp": "2025-05-17T10:30:45.123Z",
"message": "Validation failed",
"details": "Request details",
"errors": {
"fieldName1": "Error message for field 1",
"fieldName2": "Error message for field 2"
}
}
MongoDB-Specific Exception Handling
The MongoExceptionHandler class handles database-specific exceptions:

Duplicate key errors
Data integrity violations
Query execution errors
Validation Utilities
Validation helpers available:

ValidationUtil - General validation methods
ModelValidator - Model-specific validation methods
Bean validation annotations for controllers and DTOs
How to Use
In Services
java
// Example of throwing exceptions in service methods
public User getUserById(String id) {
return userRepository.findById(id)
.orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
}

public User createUser(User user) {
if (userRepository.existsByEmail(user.getEmail())) {
throw new DuplicateResourceException("User", "email", user.getEmail());
}
return userRepository.save(user);
}
In Controllers
Use validation annotations on request parameters and DTOs:

java
@PostMapping
public ResponseEntity<User> createUser(@Valid @RequestBody UserCreateRequest userRequest) {
// Service call here
}

@GetMapping("/{id}")
public ResponseEntity<User> getUserById(
@PathVariable @NotBlank(message = "User ID cannot be empty") String id) {
// Service call here
}
Testing
Unit tests are provided in ExceptionHandlingTests to verify:

Exception throwing behavior
Exception message contents
HTTP status code mapping
Integration with Existing Code
The exception handling system was designed to integrate seamlessly with the existing codebase without disrupting functionality. All repository tests continue to run smoothly with the added benefit of proper error handling.

