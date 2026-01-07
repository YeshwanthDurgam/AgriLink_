package com.agrilink.iot.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity representing an IoT device.
 */
@Entity
@Table(name = "devices")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "farmer_id", nullable = false)
    private UUID farmerId;

    @Column(name = "farm_id")
    private UUID farmId;

    @Column(name = "field_id")
    private UUID fieldId;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false, length = 50)
    private DeviceType deviceType;

    @Column(name = "device_name", nullable = false, length = 100)
    private String deviceName;

    @Column(name = "device_serial", unique = true, length = 100)
    private String deviceSerial;

    @Column(length = 100)
    private String manufacturer;

    @Column(length = 100)
    private String model;

    @Column(name = "firmware_version", length = 50)
    private String firmwareVersion;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    @Builder.Default
    private DeviceStatus status = DeviceStatus.ACTIVE;

    @Column(precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "last_seen_at")
    private LocalDateTime lastSeenAt;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Telemetry> telemetryData = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public enum DeviceType {
        SOIL_SENSOR,
        WEATHER_STATION,
        MOISTURE_SENSOR,
        TEMPERATURE_SENSOR,
        HUMIDITY_SENSOR,
        PH_SENSOR,
        IRRIGATION_CONTROLLER,
        CAMERA,
        GPS_TRACKER,
        OTHER
    }

    public enum DeviceStatus {
        ACTIVE,
        INACTIVE,
        MAINTENANCE,
        OFFLINE,
        DECOMMISSIONED
    }
}
