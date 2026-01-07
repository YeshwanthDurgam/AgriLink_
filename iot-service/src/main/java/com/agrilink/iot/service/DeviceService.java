package com.agrilink.iot.service;

import com.agrilink.common.exception.ForbiddenException;
import com.agrilink.common.exception.ResourceNotFoundException;
import com.agrilink.iot.dto.DeviceDto;
import com.agrilink.iot.dto.RegisterDeviceRequest;
import com.agrilink.iot.entity.Device;
import com.agrilink.iot.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for device operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    /**
     * Register a new device.
     */
    @Transactional
    public DeviceDto registerDevice(UUID farmerId, RegisterDeviceRequest request) {
        log.info("Registering device for farmer: {}", farmerId);

        Device device = Device.builder()
                .farmerId(farmerId)
                .farmId(request.getFarmId())
                .fieldId(request.getFieldId())
                .deviceType(request.getDeviceType())
                .deviceName(request.getDeviceName())
                .deviceSerial(request.getDeviceSerial())
                .manufacturer(request.getManufacturer())
                .model(request.getModel())
                .firmwareVersion(request.getFirmwareVersion())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .status(Device.DeviceStatus.ACTIVE)
                .build();

        Device savedDevice = deviceRepository.save(device);
        log.info("Device registered with id: {}", savedDevice.getId());

        return mapToDto(savedDevice);
    }

    /**
     * Get devices by farmer.
     */
    @Transactional(readOnly = true)
    public List<DeviceDto> getDevicesByFarmer(UUID farmerId) {
        return deviceRepository.findByFarmerId(farmerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get devices by farmer with pagination.
     */
    @Transactional(readOnly = true)
    public Page<DeviceDto> getDevicesByFarmer(UUID farmerId, Pageable pageable) {
        return deviceRepository.findByFarmerId(farmerId, pageable).map(this::mapToDto);
    }

    /**
     * Get device by ID.
     */
    @Transactional(readOnly = true)
    public DeviceDto getDevice(UUID deviceId, UUID farmerId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device", "id", deviceId));

        if (!device.getFarmerId().equals(farmerId)) {
            throw new ForbiddenException("You don't have access to this device");
        }

        return mapToDto(device);
    }

    /**
     * Update device status.
     */
    @Transactional
    public DeviceDto updateDeviceStatus(UUID deviceId, UUID farmerId, Device.DeviceStatus status) {
        log.info("Updating device {} status to {}", deviceId, status);

        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device", "id", deviceId));

        if (!device.getFarmerId().equals(farmerId)) {
            throw new ForbiddenException("You don't have permission to update this device");
        }

        device.setStatus(status);
        Device updatedDevice = deviceRepository.save(device);
        return mapToDto(updatedDevice);
    }

    /**
     * Update device last seen timestamp.
     */
    @Transactional
    public void updateLastSeen(UUID deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device", "id", deviceId));
        device.setLastSeenAt(LocalDateTime.now());
        deviceRepository.save(device);
    }

    /**
     * Delete device.
     */
    @Transactional
    public void deleteDevice(UUID deviceId, UUID farmerId) {
        log.info("Deleting device: {}", deviceId);

        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device", "id", deviceId));

        if (!device.getFarmerId().equals(farmerId)) {
            throw new ForbiddenException("You don't have permission to delete this device");
        }

        device.setStatus(Device.DeviceStatus.DECOMMISSIONED);
        deviceRepository.save(device);
    }

    private DeviceDto mapToDto(Device device) {
        return DeviceDto.builder()
                .id(device.getId())
                .farmerId(device.getFarmerId())
                .farmId(device.getFarmId())
                .fieldId(device.getFieldId())
                .deviceType(device.getDeviceType())
                .deviceName(device.getDeviceName())
                .deviceSerial(device.getDeviceSerial())
                .manufacturer(device.getManufacturer())
                .model(device.getModel())
                .firmwareVersion(device.getFirmwareVersion())
                .status(device.getStatus())
                .latitude(device.getLatitude())
                .longitude(device.getLongitude())
                .lastSeenAt(device.getLastSeenAt())
                .createdAt(device.getCreatedAt())
                .build();
    }
}
