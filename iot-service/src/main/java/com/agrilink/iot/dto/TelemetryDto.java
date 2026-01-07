package com.agrilink.iot.dto;

import com.agrilink.iot.entity.Telemetry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Telemetry information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryDto {

    private UUID id;
    private UUID deviceId;
    private Telemetry.MetricType metricType;
    private BigDecimal metricValue;
    private String unit;
    private LocalDateTime recordedAt;
}
