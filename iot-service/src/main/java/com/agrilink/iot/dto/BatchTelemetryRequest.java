package com.agrilink.iot.dto;

import com.agrilink.iot.entity.Telemetry;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for batch ingesting telemetry data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchTelemetryRequest {

    @NotNull(message = "Device ID is required")
    private UUID deviceId;

    @NotNull(message = "Readings are required")
    private List<Reading> readings;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Reading {
        private Telemetry.MetricType metricType;
        private BigDecimal metricValue;
        private String unit;
        private LocalDateTime recordedAt;
    }
}
