package com.agrilink.iot.dto;

import com.agrilink.iot.entity.Device;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for Device information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDto {

    private UUID id;
    private UUID farmerId;
    private UUID farmId;
    private UUID fieldId;
    private Device.DeviceType deviceType;
    private String deviceName;
    private String deviceSerial;
    private String manufacturer;
    private String model;
    private String firmwareVersion;
    private Device.DeviceStatus status;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime lastSeenAt;
    private LocalDateTime createdAt;
}
