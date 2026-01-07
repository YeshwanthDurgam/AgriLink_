package com.agrilink.iot.service;

import com.agrilink.iot.dto.DeviceDto;
import com.agrilink.iot.dto.RegisterDeviceRequest;
import com.agrilink.iot.entity.Device;
import com.agrilink.iot.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DeviceService.
 */
@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;

    private UUID farmerId;
    private UUID deviceId;
    private Device device;
    private RegisterDeviceRequest registerRequest;

    @BeforeEach
    void setUp() {
        farmerId = UUID.randomUUID();
        deviceId = UUID.randomUUID();

        device = Device.builder()
                .id(deviceId)
                .farmerId(farmerId)
                .deviceType(Device.DeviceType.SOIL_SENSOR)
                .deviceName("Field 1 Soil Sensor")
                .deviceSerial("SS-001")
                .status(Device.DeviceStatus.ACTIVE)
                .build();

        registerRequest = RegisterDeviceRequest.builder()
                .deviceType(Device.DeviceType.SOIL_SENSOR)
                .deviceName("Field 1 Soil Sensor")
                .deviceSerial("SS-001")
                .manufacturer("AgriSense")
                .model("SS-3000")
                .build();
    }

    @Test
    @DisplayName("Should register device successfully")
    void shouldRegisterDeviceSuccessfully() {
        // Given
        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        // When
        DeviceDto result = deviceService.registerDevice(farmerId, registerRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDeviceName()).isEqualTo("Field 1 Soil Sensor");
        assertThat(result.getFarmerId()).isEqualTo(farmerId);
        verify(deviceRepository).save(any(Device.class));
    }

    @Test
    @DisplayName("Should get devices by farmer")
    void shouldGetDevicesByFarmer() {
        // Given
        when(deviceRepository.findByFarmerId(farmerId)).thenReturn(List.of(device));

        // When
        List<DeviceDto> result = deviceService.getDevicesByFarmer(farmerId);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDeviceName()).isEqualTo("Field 1 Soil Sensor");
    }

    @Test
    @DisplayName("Should get device by ID")
    void shouldGetDeviceById() {
        // Given
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));

        // When
        DeviceDto result = deviceService.getDevice(deviceId, farmerId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(deviceId);
    }

    @Test
    @DisplayName("Should update device status")
    void shouldUpdateDeviceStatus() {
        // Given
        when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));
        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        // When
        DeviceDto result = deviceService.updateDeviceStatus(deviceId, farmerId, Device.DeviceStatus.MAINTENANCE);

        // Then
        assertThat(result).isNotNull();
        verify(deviceRepository).save(any(Device.class));
    }
}
