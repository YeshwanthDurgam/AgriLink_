package com.agrilink.order.service;

import com.agrilink.order.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Service for publishing order events.
 * In production, this would integrate with a message broker (Kafka, RabbitMQ, etc.)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventPublisher {

    /**
     * Publish order created event.
     */
    public void publishOrderCreated(UUID orderId, String orderNumber, UUID buyerId, UUID sellerId) {
        log.info("Publishing ORDER_CREATED event - orderId: {}, orderNumber: {}, buyerId: {}, sellerId: {}",
                orderId, orderNumber, buyerId, sellerId);
        // In production: send to message broker
        // kafkaTemplate.send("order-events", new OrderCreatedEvent(orderId, orderNumber, buyerId, sellerId));
    }

    /**
     * Publish order status changed event.
     */
    public void publishOrderStatusChanged(UUID orderId, Order.OrderStatus oldStatus, Order.OrderStatus newStatus) {
        log.info("Publishing ORDER_STATUS_CHANGED event - orderId: {}, from: {}, to: {}",
                orderId, oldStatus, newStatus);
        // In production: send to message broker
    }

    /**
     * Publish payment completed event.
     */
    public void publishPaymentCompleted(UUID orderId, UUID paymentId, String transactionId) {
        log.info("Publishing PAYMENT_COMPLETED event - orderId: {}, paymentId: {}, transactionId: {}",
                orderId, paymentId, transactionId);
        // In production: send to message broker
    }

    /**
     * Publish order cancelled event.
     */
    public void publishOrderCancelled(UUID orderId, String reason) {
        log.info("Publishing ORDER_CANCELLED event - orderId: {}, reason: {}", orderId, reason);
        // In production: send to message broker
    }

    /**
     * Publish order shipped event.
     */
    public void publishOrderShipped(UUID orderId, UUID buyerId) {
        log.info("Publishing ORDER_SHIPPED event - orderId: {}, buyerId: {}", orderId, buyerId);
        // In production: send to notification service
    }

    /**
     * Publish order delivered event.
     */
    public void publishOrderDelivered(UUID orderId, UUID buyerId) {
        log.info("Publishing ORDER_DELIVERED event - orderId: {}, buyerId: {}", orderId, buyerId);
        // In production: send to notification service
    }
}
