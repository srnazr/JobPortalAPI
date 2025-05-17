package com.serena.jobportal.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.*;

import com.serena.jobportal.model.Job;

/**
 * Validates that a salary range is logically consistent (min <= max)
 */
@Documented
@Constraint(validatedBy = ValidSalaryRangeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSalaryRange {
    String message() default "Minimum salary cannot be greater than maximum salary";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

/**
 * Validator for salary range
 * Note: This validates at the class level, not field level
 */
class ValidSalaryRangeValidator implements ConstraintValidator<ValidSalaryRange, Job> {

    @Override
    public boolean isValid(Job job, ConstraintValidatorContext context) {
        if (job == null || job.getRange() == null) {
            return true; // Let @NotNull handle this
        }

        Job.SalaryRange range = job.getRange();
        return range.getMin() <= range.getMax();
    }
}