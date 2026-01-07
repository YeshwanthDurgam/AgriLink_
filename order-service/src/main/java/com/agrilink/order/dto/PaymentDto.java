package com.agrilink.order.dto;

import com.agrilink.order.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Payment information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private UUID id;
    private UUID orderId;
    private String paymentMethod;
    private Payment.PaymentStatus paymentStatus;
    private BigDecimal amount;
    private String currency;
    private String transactionId;
    private String paymentGateway;
    private LocalDateTime paidAt;
    private LocalDateTime createdAt;
}
