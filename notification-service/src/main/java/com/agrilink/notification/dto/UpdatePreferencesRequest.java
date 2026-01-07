package com.agrilink.notification.dto;

import lombok.*;

/**
 * Request DTO for updating notification preferences.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePreferencesRequest {

    private Boolean emailEnabled;
    private Boolean smsEnabled;
    private Boolean pushEnabled;
    private Boolean orderUpdates;
    private Boolean listingUpdates;
    private Boolean priceAlerts;
    private Boolean weatherAlerts;
    private Boolean iotAlerts;
    private Boolean marketing;
}
