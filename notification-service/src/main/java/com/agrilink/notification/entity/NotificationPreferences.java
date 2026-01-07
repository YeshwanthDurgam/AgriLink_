package com.agrilink.notification.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Notification Preferences entity.
 */
@Entity
@Table(name = "notification_preferences")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "email_enabled", nullable = false)
    @Builder.Default
    private boolean emailEnabled = true;

    @Column(name = "sms_enabled", nullable = false)
    @Builder.Default
    private boolean smsEnabled = false;

    @Column(name = "push_enabled", nullable = false)
    @Builder.Default
    private boolean pushEnabled = true;

    @Column(name = "order_updates", nullable = false)
    @Builder.Default
    private boolean orderUpdates = true;

    @Column(name = "listing_updates", nullable = false)
    @Builder.Default
    private boolean listingUpdates = true;

    @Column(name = "price_alerts", nullable = false)
    @Builder.Default
    private boolean priceAlerts = true;

    @Column(name = "weather_alerts", nullable = false)
    @Builder.Default
    private boolean weatherAlerts = true;

    @Column(name = "iot_alerts", nullable = false)
    @Builder.Default
    private boolean iotAlerts = true;

    @Column(nullable = false)
    @Builder.Default
    private boolean marketing = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
