package com.serena.jobportal.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;
import java.util.Arrays;
import java.util.List;

/**
 * Validation annotation for user role
 */
@Documented
@Constraint(validatedBy = ValidUserRoleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserRole {
    String message() default "Invalid user role";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

/**
 * Validator implementation for user role
 */
class ValidUserRoleValidator implements ConstraintValidator<ValidUserRole, String> {

    private static final List<String> VALID_ROLES = Arrays.asList(
            "ADMIN", "RECRUITER", "CANDIDATE"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return false; // Use @NotBlank for empty check
        }

        return VALID_ROLES.contains(value);
    }
}