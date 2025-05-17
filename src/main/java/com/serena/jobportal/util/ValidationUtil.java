package com.serena.jobportal.util;

import com.serena.jobportal.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class ValidationUtil {

    // Email validation regex pattern
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Phone number validation regex pattern
    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?[0-9]{10,15}$");

    /**
     * Validates that a string is not null or empty
     *
     * @param value The string to validate
     * @param fieldName The name of the field being validated
     * @throws BadRequestException if value is null or empty
     */
    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException(fieldName + " cannot be empty");
        }
    }

    /**
     * Validates that a string is a valid email address
     *
     * @param email The email to validate
     * @throws BadRequestException if email is invalid
     */
    public static void validateEmail(String email) {
        validateNotEmpty(email, "Email");

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new BadRequestException("Invalid email format");
        }
    }

    /**
     * Validates that a string is a valid phone number
     *
     * @param phone The phone number to validate
     * @throws BadRequestException if phone number is invalid
     */
    public static void validatePhone(String phone) {
        validateNotEmpty(phone, "Phone number");

        if (!PHONE_PATTERN.matcher(phone).matches()) {
            throw new BadRequestException("Invalid phone number format");
        }
    }

    /**
     * Validates that a numeric value is positive
     *
     * @param value The value to validate
     * @param fieldName The name of the field being validated
     * @throws BadRequestException if value is negative or zero
     */
    public static void validatePositive(double value, String fieldName) {
        if (value <= 0) {
            throw new BadRequestException(fieldName + " must be positive");
        }
    }

    /**
     * Validates that a salary range is valid
     *
     * @param min The minimum salary
     * @param max The maximum salary
     * @throws BadRequestException if range is invalid
     */
    public static void validateSalaryRange(double min, double max) {
        validatePositive(min, "Minimum salary");
        validatePositive(max, "Maximum salary");

        if (min > max) {
            throw new BadRequestException("Minimum salary cannot be greater than maximum salary");
        }
    }

    /**
     * Validates an employment type
     *
     * @param type The employment type
     * @throws BadRequestException if type is invalid
     */
    public static void validateEmploymentType(String type) {
        validateNotEmpty(type, "Employment type");

        // Add allowed types as per your application needs
        String[] allowedTypes = {"FULL_TIME", "PART_TIME", "CONTRACT", "REMOTE", "INTERNSHIP"};
        boolean valid = false;

        for (String allowedType : allowedTypes) {
            if (allowedType.equals(type)) {
                valid = true;
                break;
            }
        }

        if (!valid) {
            throw new BadRequestException("Invalid employment type: " + type);
        }
    }

    /**
     * Validates a user role
     *
     * @param role The user role
     * @throws BadRequestException if role is invalid
     */
    public static void validateUserRole(String role) {
        validateNotEmpty(role, "User role");

        String[] allowedRoles = {"ADMIN", "RECRUITER", "CANDIDATE"};
        boolean valid = false;

        for (String allowedRole : allowedRoles) {
            if (allowedRole.equals(role)) {
                valid = true;
                break;
            }
        }

        if (!valid) {
            throw new BadRequestException("Invalid user role: " + role);
        }
    }
}