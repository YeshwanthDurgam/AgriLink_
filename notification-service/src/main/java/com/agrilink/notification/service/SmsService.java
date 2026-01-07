package com.agrilink.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * SMS Service for sending text messages.
 * Mock implementation - integrate with Twilio/AWS SNS in production.
 */
@Slf4j
@Service
public class SmsService {

    @Value("${notification.sms.enabled}")
    private boolean smsEnabled;

    @Value("${notification.sms.provider}")
    private String smsProvider;

    /**
     * Send an SMS asynchronously.
     */
    @Async
    public void sendSms(String phoneNumber, String message) {
        if (!smsEnabled) {
            log.info("SMS sending disabled. Would have sent SMS to {} : {}", phoneNumber, message);
            return;
        }

        try {
            log.info("Sending SMS to {} via {}", phoneNumber, smsProvider);

            // Mock implementation
            // In production, integrate with Twilio, AWS SNS, or other SMS provider
            switch (smsProvider.toLowerCase()) {
                case "twilio" -> sendViaTwilio(phoneNumber, message);
                case "aws" -> sendViaAwsSns(phoneNumber, message);
                default -> log.warn("Unknown SMS provider: {}", smsProvider);
            }

            log.info("SMS sent successfully to {}", phoneNumber);
        } catch (Exception e) {
            log.error("Failed to send SMS to {}: {}", phoneNumber, e.getMessage());
            throw new RuntimeException("Failed to send SMS", e);
        }
    }

    private void sendViaTwilio(String phoneNumber, String message) {
        // Mock Twilio implementation
        log.debug("Twilio: Sending '{}' to {}", message, phoneNumber);
        // Actual implementation would use Twilio SDK
    }

    private void sendViaAwsSns(String phoneNumber, String message) {
        // Mock AWS SNS implementation
        log.debug("AWS SNS: Sending '{}' to {}", message, phoneNumber);
        // Actual implementation would use AWS SDK
    }

    /**
     * Send OTP via SMS.
     */
    @Async
    public void sendOtp(String phoneNumber, String otp) {
        String message = String.format("Your AgriLink verification code is: %s. Valid for 10 minutes.", otp);
        sendSms(phoneNumber, message);
    }

    /**
     * Send order update SMS.
     */
    @Async
    public void sendOrderUpdateSms(String phoneNumber, String orderNumber, String status) {
        String message = String.format("AgriLink: Order %s status updated to %s", orderNumber, status);
        sendSms(phoneNumber, message);
    }
}
