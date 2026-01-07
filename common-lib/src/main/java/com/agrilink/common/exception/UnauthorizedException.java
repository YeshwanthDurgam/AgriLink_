package com.agrilink.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when authentication fails or user is not authenticated.
 */
public class UnauthorizedException extends BaseException {
    
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }
    
    public UnauthorizedException() {
        super("Authentication required", HttpStatus.UNAUTHORIZED, "UNAUTHORIZED");
    }
}
