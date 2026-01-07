package com.agrilink.iot.dto;

import com.agrilink.iot.entity.Alert;
import com.agrilink.iot.entity.Telemetry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Alert information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertDto {

    private UUID id;
    private UUID deviceId;
    private String deviceName;
    private UUID farmerId;
    private Alert.AlertType alertType;
    private Alert.Severity severity;
    private String message;
    private Telemetry.MetricType metricType;
    private BigDecimal metricValue;
    private BigDecimal thresholdValue;
    private boolean acknowledged;
    private LocalDateTime acknowledgedAt;
    private UUID acknowledgedBy;
    private LocalDateTime createdAt;
}
