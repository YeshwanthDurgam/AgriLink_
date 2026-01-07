package com.agrilink.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Email Service for sending emails.
 * Mock implementation for development.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${notification.email.from}")
    private String fromAddress;

    @Value("${notification.email.enabled}")
    private boolean emailEnabled;

    /**
     * Send an email asynchronously.
     */
    @Async
    public void sendEmail(String to, String subject, String body) {
        if (!emailEnabled) {
            log.info("Email sending disabled. Would have sent email to {} with subject: {}", to, subject);
            return;
        }

        try {
            log.info("Sending email to {} with subject: {}", to, subject);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            // Mock send - in production, uncomment the next line
            // mailSender.send(message);

            log.info("Email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }

    /**
     * Send a welcome email.
     */
    @Async
    public void sendWelcomeEmail(String to, String userName) {
        String subject = "Welcome to AgriLink!";
        String body = String.format("""
                Hello %s,
                
                Welcome to AgriLink! We're excited to have you join our community.
                
                AgriLink connects farmers directly with buyers, making agricultural commerce 
                easier and more efficient for everyone.
                
                Get started by:
                1. Completing your profile
                2. Exploring the marketplace
                3. Setting up your farm (if you're a farmer)
                
                If you have any questions, feel free to reach out to our support team.
                
                Happy farming!
                The AgriLink Team
                """, userName);

        sendEmail(to, subject, body);
    }

    /**
     * Send an order confirmation email.
     */
    @Async
    public void sendOrderConfirmationEmail(String to, String orderNumber, String total) {
        String subject = "Order Confirmation - " + orderNumber;
        String body = String.format("""
                Your order has been confirmed!
                
                Order Number: %s
                Total: %s
                
                You can track your order status in your AgriLink account.
                
                Thank you for shopping with AgriLink!
                """, orderNumber, total);

        sendEmail(to, subject, body);
    }
}
