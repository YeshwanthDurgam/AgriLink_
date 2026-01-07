package com.agrilink.order.service;

import com.agrilink.common.exception.BadRequestException;
import com.agrilink.common.exception.ResourceNotFoundException;
import com.agrilink.order.dto.PaymentDto;
import com.agrilink.order.dto.ProcessPaymentRequest;
import com.agrilink.order.entity.Order;
import com.agrilink.order.entity.Payment;
import com.agrilink.order.repository.OrderRepository;
import com.agrilink.order.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for payment operations.
 * This is a mock implementation - in production, integrate with actual payment gateways.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    /**
     * Process payment for an order.
     */
    @Transactional
    public PaymentDto processPayment(ProcessPaymentRequest request) {
        log.info("Processing payment for order: {}", request.getOrderId());

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", request.getOrderId()));

        // Validate order status
        if (order.getStatus() != Order.OrderStatus.PENDING && 
            order.getStatus() != Order.OrderStatus.CONFIRMED) {
            throw new BadRequestException("Cannot process payment for order in status: " + order.getStatus());
        }

        // Validate amount
        if (request.getAmount().compareTo(order.getTotalAmount()) != 0) {
            throw new BadRequestException("Payment amount does not match order total");
        }

        // Create payment record
        Payment payment = Payment.builder()
                .order(order)
                .paymentMethod(request.getPaymentMethod())
                .amount(request.getAmount())
                .currency(request.getCurrency() != null ? request.getCurrency() : "USD")
                .paymentGateway(request.getPaymentGateway() != null ? request.getPaymentGateway() : "MOCK")
                .paymentStatus(Payment.PaymentStatus.PROCESSING)
                .build();

        // Mock payment processing
        boolean paymentSuccess = mockPaymentGateway(payment);

        if (paymentSuccess) {
            payment.setPaymentStatus(Payment.PaymentStatus.COMPLETED);
            payment.setTransactionId(generateTransactionId());
            payment.setPaidAt(LocalDateTime.now());

            // Update order status
            order.setStatus(Order.OrderStatus.CONFIRMED);
            orderRepository.save(order);
        } else {
            payment.setPaymentStatus(Payment.PaymentStatus.FAILED);
        }

        Payment savedPayment = paymentRepository.save(payment);
        log.info("Payment {} for order {}", 
                paymentSuccess ? "completed" : "failed", order.getOrderNumber());

        return mapToDto(savedPayment);
    }

    /**
     * Get payment by ID.
     */
    @Transactional(readOnly = true)
    public PaymentDto getPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));
        return mapToDto(payment);
    }

    /**
     * Refund payment.
     */
    @Transactional
    public PaymentDto refundPayment(UUID paymentId) {
        log.info("Processing refund for payment: {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", paymentId));

        if (payment.getPaymentStatus() != Payment.PaymentStatus.COMPLETED) {
            throw new BadRequestException("Can only refund completed payments");
        }

        // Mock refund processing
        payment.setPaymentStatus(Payment.PaymentStatus.REFUNDED);

        // Update order status
        Order order = payment.getOrder();
        order.setStatus(Order.OrderStatus.REFUNDED);
        orderRepository.save(order);

        Payment updatedPayment = paymentRepository.save(payment);
        return mapToDto(updatedPayment);
    }

    /**
     * Mock payment gateway - always succeeds.
     * In production, replace with actual payment gateway integration.
     */
    private boolean mockPaymentGateway(Payment payment) {
        // Simulate processing delay
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // Always return success for mock
        return true;
    }

    private String generateTransactionId() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }

    public PaymentDto mapToDto(Payment payment) {
        return PaymentDto.builder()
                .id(payment.getId())
                .orderId(payment.getOrder().getId())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .transactionId(payment.getTransactionId())
                .paymentGateway(payment.getPaymentGateway())
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
