package com.agrilink.user.exception;

import com.agrilink.common.exception.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception handler for user-service.
 */
@Slf4j
@RestControllerAdvice
public class UserExceptionHandler extends GlobalExceptionHandler {
    // Inherits all handlers from GlobalExceptionHandler
    // Add user-service specific handlers here if needed
}
