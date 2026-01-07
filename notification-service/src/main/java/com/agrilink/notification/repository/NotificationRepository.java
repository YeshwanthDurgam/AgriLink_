package com.agrilink.notification.repository;

import com.agrilink.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for Notification entity.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    Page<Notification> findByUserId(UUID userId, Pageable pageable);

    Page<Notification> findByUserIdAndRead(UUID userId, boolean read, Pageable pageable);

    List<Notification> findByUserIdAndReadFalse(UUID userId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.read = false")
    long countUnreadByUserId(@Param("userId") UUID userId);

    List<Notification> findByStatus(Notification.Status status);

    @Query("SELECT n FROM Notification n WHERE n.status = 'FAILED' AND n.retryCount < :maxRetries")
    List<Notification> findFailedNotificationsForRetry(@Param("maxRetries") int maxRetries);

    Page<Notification> findByUserIdAndNotificationType(UUID userId, Notification.NotificationType type, Pageable pageable);
}
