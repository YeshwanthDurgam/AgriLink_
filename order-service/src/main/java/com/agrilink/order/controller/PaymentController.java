package com.agrilink.order.controller;

import com.agrilink.common.dto.ApiResponse;
import com.agrilink.order.dto.PaymentDto;
import com.agrilink.order.dto.ProcessPaymentRequest;
import com.agrilink.order.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST Controller for payment operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Process payment.
     * POST /api/v1/payments
     */
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentDto>> processPayment(
            @Valid @RequestBody ProcessPaymentRequest request) {
        PaymentDto payment = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Payment processed", payment));
    }

    /**
     * Get payment by ID.
     * GET /api/v1/payments/{paymentId}
     */
    @GetMapping("/{paymentId}")
    public ResponseEntity<ApiResponse<PaymentDto>> getPayment(@PathVariable UUID paymentId) {
        PaymentDto payment = paymentService.getPayment(paymentId);
        return ResponseEntity.ok(ApiResponse.success(payment));
    }

    /**
     * Refund payment.
     * POST /api/v1/payments/{paymentId}/refund
     */
    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<ApiResponse<PaymentDto>> refundPayment(@PathVariable UUID paymentId) {
        PaymentDto payment = paymentService.refundPayment(paymentId);
        return ResponseEntity.ok(ApiResponse.success("Payment refunded", payment));
    }
}
