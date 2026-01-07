package com.agrilink.iot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing telemetry data from IoT devices.
 */
@Entity
@Table(name = "telemetry")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Telemetry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Enumerated(EnumType.STRING)
    @Column(name = "metric_type", nullable = false, length = 50)
    private MetricType metricType;

    @Column(name = "metric_value", nullable = false, precision = 14, scale = 4)
    private BigDecimal metricValue;

    @Column(length = 20)
    private String unit;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum MetricType {
        TEMPERATURE,
        HUMIDITY,
        SOIL_MOISTURE,
        SOIL_PH,
        SOIL_NITROGEN,
        SOIL_PHOSPHORUS,
        SOIL_POTASSIUM,
        LIGHT_INTENSITY,
        RAINFALL,
        WIND_SPEED,
        WIND_DIRECTION,
        AIR_PRESSURE,
        CO2_LEVEL,
        BATTERY_LEVEL,
        SIGNAL_STRENGTH,
        WATER_FLOW,
        WATER_LEVEL,
        OTHER
    }
}
