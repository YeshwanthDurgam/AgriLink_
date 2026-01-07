package com.agrilink.order.exception;

import com.agrilink.common.exception.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception handler for order-service.
 */
@Slf4j
@RestControllerAdvice
public class OrderExceptionHandler extends GlobalExceptionHandler {
    // Inherits all handlers from GlobalExceptionHandler
}
