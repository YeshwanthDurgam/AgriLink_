package com.agrilink.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base exception class for all custom exceptions in AgriLink.
 * Provides HTTP status and error code for consistent error handling.
 */
@Getter
public abstract class BaseException extends RuntimeException {
    
    private final HttpStatus status;
    private final String errorCode;
    
    protected BaseException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
    
    protected BaseException(String message, Throwable cause, HttpStatus status, String errorCode) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
    }
}
