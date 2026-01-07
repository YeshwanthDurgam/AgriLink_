package com.agrilink.order.service;

import com.agrilink.order.dto.CreateOrderRequest;
import com.agrilink.order.dto.OrderDto;
import com.agrilink.order.entity.Order;
import com.agrilink.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderService.
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private OrderService orderService;

    private UUID buyerId;
    private UUID sellerId;
    private UUID orderId;
    private UUID listingId;
    private Order order;
    private CreateOrderRequest createRequest;

    @BeforeEach
    void setUp() {
        buyerId = UUID.randomUUID();
        sellerId = UUID.randomUUID();
        orderId = UUID.randomUUID();
        listingId = UUID.randomUUID();

        order = Order.builder()
                .id(orderId)
                .orderNumber("ORD-20260101120000-1234")
                .buyerId(buyerId)
                .sellerId(sellerId)
                .listingId(listingId)
                .totalAmount(new BigDecimal("250.00"))
                .currency("USD")
                .status(Order.OrderStatus.PENDING)
                .build();

        createRequest = CreateOrderRequest.builder()
                .listingId(listingId)
                .quantity(new BigDecimal("100"))
                .quantityUnit("KG")
                .shippingAddress("123 Farm Road")
                .shippingCity("Farmville")
                .shippingCountry("USA")
                .build();
    }

    @Test
    @DisplayName("Should create order successfully")
    void shouldCreateOrderSuccessfully() {
        // Given
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        OrderDto result = orderService.createOrder(buyerId, createRequest, sellerId, "Fresh Tomatoes", new BigDecimal("2.50"));

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBuyerId()).isEqualTo(buyerId);
        assertThat(result.getSellerId()).isEqualTo(sellerId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Should get order by ID")
    void shouldGetOrderById() {
        // Given
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When
        OrderDto result = orderService.getOrder(orderId, buyerId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(orderId);
    }

    @Test
    @DisplayName("Should update order status")
    void shouldUpdateOrderStatus() {
        // Given
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        OrderDto result = orderService.confirmOrder(orderId, sellerId);

        // Then
        assertThat(result).isNotNull();
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Should cancel order")
    void shouldCancelOrder() {
        // Given
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.setStatus(Order.OrderStatus.CANCELLED);
            return savedOrder;
        });

        // When
        OrderDto result = orderService.cancelOrder(orderId, buyerId, "Changed my mind");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(Order.OrderStatus.CANCELLED);
    }
}
