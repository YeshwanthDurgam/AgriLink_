package com.agrilink.notification.dto;

import lombok.*;

import java.util.UUID;

/**
 * DTO for notification preferences.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPreferencesDto {

    private UUID id;
    private UUID userId;
    private boolean emailEnabled;
    private boolean smsEnabled;
    private boolean pushEnabled;
    private boolean orderUpdates;
    private boolean listingUpdates;
    private boolean priceAlerts;
    private boolean weatherAlerts;
    private boolean iotAlerts;
    private boolean marketing;
}
