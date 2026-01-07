package com.agrilink.iot.dto;

import com.agrilink.iot.entity.Device;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for registering a new device.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDeviceRequest {

    private UUID farmId;
    private UUID fieldId;

    @NotNull(message = "Device type is required")
    private Device.DeviceType deviceType;

    @NotBlank(message = "Device name is required")
    private String deviceName;

    private String deviceSerial;
    private String manufacturer;
    private String model;
    private String firmwareVersion;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
