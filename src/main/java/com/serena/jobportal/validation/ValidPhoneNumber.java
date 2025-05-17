package com.serena.jobportal.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;
import java.util.regex.Pattern;

/**
 * Validation annotation for phone numbers
 */
@Documented
@Constraint(validatedBy = ValidPhoneNumberValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {
    String message() default "Invalid phone number format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

/**
 * Validator implementation for phone numbers
 */
class ValidPhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {
    // Class renamed from PhoneNumberValidator to ValidPhoneNumberValidator to match usage in model

    private static final Pattern PHONE_PATTERN =
            Pattern.compile("^\\+?[0-9]{10,15}$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true; // Empty is valid, use @NotBlank if needed
        }

        return PHONE_PATTERN.matcher(value).matches();
    }
}