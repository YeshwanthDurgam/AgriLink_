package com.agrilink.order.repository;

import com.agrilink.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Order entity.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findByOrderNumber(String orderNumber);

    Page<Order> findByBuyerId(UUID buyerId, Pageable pageable);

    Page<Order> findBySellerId(UUID sellerId, Pageable pageable);

    Page<Order> findByBuyerIdAndStatus(UUID buyerId, Order.OrderStatus status, Pageable pageable);

    Page<Order> findBySellerIdAndStatus(UUID sellerId, Order.OrderStatus status, Pageable pageable);

    List<Order> findByStatus(Order.OrderStatus status);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.buyerId = :buyerId")
    long countByBuyerId(@Param("buyerId") UUID buyerId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.sellerId = :sellerId")
    long countBySellerId(@Param("sellerId") UUID sellerId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.sellerId = :sellerId AND o.status = :status")
    long countBySellerIdAndStatus(@Param("sellerId") UUID sellerId, @Param("status") Order.OrderStatus status);

    @Query("SELECT o FROM Order o WHERE o.buyerId = :userId OR o.sellerId = :userId ORDER BY o.createdAt DESC")
    Page<Order> findByBuyerIdOrSellerId(@Param("userId") UUID userId, Pageable pageable);
}
