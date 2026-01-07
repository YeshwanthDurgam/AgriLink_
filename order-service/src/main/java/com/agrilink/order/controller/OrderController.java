package com.agrilink.order.controller;

import com.agrilink.common.dto.ApiResponse;
import com.agrilink.common.dto.PagedResponse;
import com.agrilink.order.dto.CreateOrderRequest;
import com.agrilink.order.dto.OrderDto;
import com.agrilink.order.entity.Order;
import com.agrilink.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * REST Controller for order operations.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Create a new order.
     * POST /api/v1/orders
     */
    @PostMapping
    @PreAuthorize("hasRole('BUYER') or hasRole('FARMER')")
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(
            Authentication authentication,
            @Valid @RequestBody CreateOrderRequest request,
            @RequestParam UUID sellerId,
            @RequestParam String productName,
            @RequestParam BigDecimal unitPrice) {
        UUID buyerId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        OrderDto order = orderService.createOrder(buyerId, request, sellerId, productName, unitPrice);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order created successfully", order));
    }

    /**
     * Get order by ID.
     * GET /api/v1/orders/{orderId}
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrder(
            Authentication authentication,
            @PathVariable UUID orderId) {
        UUID userId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        OrderDto order = orderService.getOrder(orderId, userId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    /**
     * Get order by order number.
     * GET /api/v1/orders/number/{orderNumber}
     */
    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderByNumber(
            Authentication authentication,
            @PathVariable String orderNumber) {
        UUID userId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        OrderDto order = orderService.getOrderByNumber(orderNumber, userId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    /**
     * Get my orders as buyer.
     * GET /api/v1/orders/my/purchases
     */
    @GetMapping("/my/purchases")
    public ResponseEntity<ApiResponse<PagedResponse<OrderDto>>> getMyPurchases(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        UUID buyerId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<OrderDto> orders = orderService.getOrdersForBuyer(buyerId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(orders)));
    }

    /**
     * Get my orders as seller.
     * GET /api/v1/orders/my/sales
     */
    @GetMapping("/my/sales")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<PagedResponse<OrderDto>>> getMySales(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        UUID sellerId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<OrderDto> orders = orderService.getOrdersForSeller(sellerId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PagedResponse.of(orders)));
    }

    /**
     * Cancel order.
     * POST /api/v1/orders/{orderId}/cancel
     */
    @PostMapping("/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderDto>> cancelOrder(
            Authentication authentication,
            @PathVariable UUID orderId,
            @RequestParam(required = false) String reason) {
        UUID userId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        OrderDto order = orderService.cancelOrder(orderId, userId, reason != null ? reason : "Cancelled by user");
        return ResponseEntity.ok(ApiResponse.success("Order cancelled", order));
    }

    /**
     * Confirm order (seller only).
     * POST /api/v1/orders/{orderId}/confirm
     */
    @PostMapping("/{orderId}/confirm")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<OrderDto>> confirmOrder(
            Authentication authentication,
            @PathVariable UUID orderId) {
        UUID sellerId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        OrderDto order = orderService.confirmOrder(orderId, sellerId);
        return ResponseEntity.ok(ApiResponse.success("Order confirmed", order));
    }

    /**
     * Ship order (seller only).
     * POST /api/v1/orders/{orderId}/ship
     */
    @PostMapping("/{orderId}/ship")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<OrderDto>> shipOrder(
            Authentication authentication,
            @PathVariable UUID orderId) {
        UUID sellerId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        OrderDto order = orderService.shipOrder(orderId, sellerId);
        return ResponseEntity.ok(ApiResponse.success("Order shipped", order));
    }

    /**
     * Mark as delivered (seller only).
     * POST /api/v1/orders/{orderId}/deliver
     */
    @PostMapping("/{orderId}/deliver")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<ApiResponse<OrderDto>> deliverOrder(
            Authentication authentication,
            @PathVariable UUID orderId) {
        UUID sellerId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        OrderDto order = orderService.deliverOrder(orderId, sellerId);
        return ResponseEntity.ok(ApiResponse.success("Order marked as delivered", order));
    }

    /**
     * Complete order.
     * POST /api/v1/orders/{orderId}/complete
     */
    @PostMapping("/{orderId}/complete")
    public ResponseEntity<ApiResponse<OrderDto>> completeOrder(
            Authentication authentication,
            @PathVariable UUID orderId) {
        UUID userId = UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        OrderDto order = orderService.completeOrder(orderId, userId);
        return ResponseEntity.ok(ApiResponse.success("Order completed", order));
    }
}
