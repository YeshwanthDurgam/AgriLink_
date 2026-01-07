package com.agrilink.iot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing alert rules for telemetry monitoring.
 */
@Entity
@Table(name = "alert_rules")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertRule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "farmer_id", nullable = false)
    private UUID farmerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id")
    private Device device;

    @Enumerated(EnumType.STRING)
    @Column(name = "metric_type", nullable = false, length = 50)
    private Telemetry.MetricType metricType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Condition condition;

    @Column(name = "threshold_value", nullable = false, precision = 14, scale = 4)
    private BigDecimal thresholdValue;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Alert.Severity severity;

    @Column
    @Builder.Default
    private boolean enabled = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum Condition {
        GREATER_THAN,
        LESS_THAN,
        EQUALS,
        NOT_EQUALS,
        GREATER_OR_EQUAL,
        LESS_OR_EQUAL
    }
}
