package com.serena.jobportal.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validation annotation for employment type
 */
@Documented
@Constraint(validatedBy = ValidEmploymentTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmploymentType {
    String message() default "Invalid employment type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

/**
 * Validator implementation for employment type
 */
class ValidEmploymentTypeValidator implements ConstraintValidator<ValidEmploymentType, String> {

    private static final List<String> VALID_TYPES = Arrays.asList(
            "FULL_TIME", "PART_TIME", "CONTRACT", "REMOTE", "INTERNSHIP"
    );

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return false; // Use @NotBlank for empty check
        }

        return VALID_TYPES.contains(value);
    }
}