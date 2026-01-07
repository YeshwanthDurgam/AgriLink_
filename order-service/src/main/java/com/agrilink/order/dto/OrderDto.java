package com.agrilink.order.dto;

import com.agrilink.order.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for Order information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private UUID id;
    private String orderNumber;
    private UUID buyerId;
    private UUID sellerId;
    private UUID listingId;
    private Order.OrderStatus status;
    private BigDecimal totalAmount;
    private String currency;
    private String shippingAddress;
    private String shippingCity;
    private String shippingState;
    private String shippingPostalCode;
    private String shippingCountry;
    private String shippingPhone;
    private String notes;
    private List<OrderItemDto> items;
    private PaymentDto latestPayment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
