package com.agrilink.iot.dto;

import com.agrilink.iot.entity.Telemetry;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for ingesting telemetry data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryIngestRequest {

    @NotNull(message = "Device ID is required")
    private UUID deviceId;

    @NotNull(message = "Metric type is required")
    private Telemetry.MetricType metricType;

    @NotNull(message = "Metric value is required")
    private BigDecimal metricValue;

    private String unit;
    private LocalDateTime recordedAt;
}
