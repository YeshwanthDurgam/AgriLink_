package com.agrilink.marketplace.exception;

import com.agrilink.common.exception.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception handler for marketplace-service.
 */
@Slf4j
@RestControllerAdvice
public class MarketplaceExceptionHandler extends GlobalExceptionHandler {
    // Inherits all handlers from GlobalExceptionHandler
}
