package com.serena.jobportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Unauthorized exception
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends JobPortalException {
    public UnauthorizedException(String message) {
        super(message);
    }
}