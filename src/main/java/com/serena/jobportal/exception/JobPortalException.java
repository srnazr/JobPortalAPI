package com.serena.jobportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Base exception class
public class JobPortalException extends RuntimeException {
    public JobPortalException(String message) {
        super(message);
    }

    public JobPortalException(String message, Throwable cause) {
        super(message, cause);
    }
}