package com.serena.jobportal.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class ValidationErrorResponse extends ErrorResponse {
    private Object errors;

    public ValidationErrorResponse(Date timestamp, String message, String details, Object errors) {
        super(timestamp, message, details);
        this.errors = errors;
    }
}