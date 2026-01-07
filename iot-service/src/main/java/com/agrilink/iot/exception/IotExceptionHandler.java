package com.agrilink.iot.exception;

import com.agrilink.common.exception.GlobalExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception handler for iot-service.
 */
@Slf4j
@RestControllerAdvice
public class IotExceptionHandler extends GlobalExceptionHandler {
    // Inherits all handlers from GlobalExceptionHandler
}
