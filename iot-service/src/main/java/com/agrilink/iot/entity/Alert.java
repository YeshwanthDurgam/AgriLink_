package com.agrilink.iot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing an alert generated from telemetry data.
 */
@Entity
@Table(name = "alerts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(name = "farmer_id", nullable = false)
    private UUID farmerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false, length = 50)
    private AlertType alertType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Severity severity;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "metric_type", length = 50)
    private Telemetry.MetricType metricType;

    @Column(name = "metric_value", precision = 14, scale = 4)
    private BigDecimal metricValue;

    @Column(name = "threshold_value", precision = 14, scale = 4)
    private BigDecimal thresholdValue;

    @Column
    @Builder.Default
    private boolean acknowledged = false;

    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;

    @Column(name = "acknowledged_by")
    private UUID acknowledgedBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum AlertType {
        THRESHOLD_EXCEEDED,
        THRESHOLD_BELOW,
        DEVICE_OFFLINE,
        DEVICE_MALFUNCTION,
        LOW_BATTERY,
        CONNECTION_LOST,
        ANOMALY_DETECTED,
        MAINTENANCE_REQUIRED
    }

    public enum Severity {
        INFO,
        WARNING,
        CRITICAL
    }
}
