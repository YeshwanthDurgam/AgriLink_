package com.agrilink.farm.exception;

import com.agrilink.common.exception.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception handler for farm-service.
 */
@Slf4j
@RestControllerAdvice
public class FarmExceptionHandler extends GlobalExceptionHandler {
    // Inherits all handlers from GlobalExceptionHandler
}
